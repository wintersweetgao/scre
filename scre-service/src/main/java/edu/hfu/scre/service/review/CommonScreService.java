package edu.hfu.scre.service.review;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import edu.hfu.scre.entity.*;
import edu.hfu.scre.util.Constant;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import edu.hfu.scre.dao.base.BaseDaoImpl;
import edu.hfu.scre.dao.review.CommonScreDao;
import edu.hfu.scre.dao.sysset.ReviewLogDao;


import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.ScreStandardService;
import edu.hfu.scre.util.CacheData;
import edu.hfu.scre.util.DateUtil;
import edu.hfu.scre.util.FormatUtil;
import org.springframework.util.ResourceUtils;

@Service
@Transactional
public class CommonScreService {
	@Resource
	private BaseDaoImpl dao;
	@Resource
	CommonScreDao commonDao;
	@Resource
	ReviewLogDao logDao;
	@Resource
	ScreStandardService standService;
	/**
	 * 单表通用查询
	 * @param <T>
	 * @param t
	 * @param pageNo
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getContentByCon(T t,int pageNo,int maxResults) throws Exception{
		return dao.queryBean(t, pageNo, maxResults);
	}
	public <T> List<T> getContentByCon(T t) throws Exception{
		return dao.queryBean(t);
	}
	public <T> int getCountByCon(T t) throws Exception{
		return dao.queryBeanCount(t);
	}
	/**
	 * 获取某个人的科研成果
	 * @param className
	 * @param screType
	 * @param userCode
	 * @param belongCycle
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getScreCount(String className,String screType,
			String userCode,String belongCycle) throws Exception{
		return commonDao.getScreCount(className, screType, userCode, belongCycle);
	}
	
	/**
	 * 科研  通用保存类
	 * @param <T>
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> String saveScre(T t)throws Exception {
		if (RptBase.class.isAssignableFrom(t.getClass())) {//判断 要保存的实体bean是否继承了RptBase
			RptBase base=(RptBase)t;
			String screType=base.getScreType();
			String screTopic=base.getScreTopic();
			Integer inOrder=base.getInOrder();
			//计算科研积分
			Integer expectedMark=standService.getStandardMark(screType,screTopic,inOrder,base.getBelongCycle());
			base.setExpectedMark(expectedMark);
			base.setStatus("BApprove");
			base.setCreateTime(new Date());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			//用户代码等
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				base.setStaffName(user.getStaffName());
				base.setStaffTitle(user.getStaffTitle());
				base.setStaffParentDepart(user.getStaffParentDepart());
				base.setStaffDepart(user.getStaffDepart());
				base.setUserCode(user.getUserCode());
			}
			//竟然用父类也可以
			int recordId=(int) dao.save(base);
			logDao.addReviewLog(FormatUtil.getEntityName(t.getClass().getName()), recordId, "通过","AApprove", "BApprove", "新增:"+screType);
			return "succ";
		}else {
			throw new java.lang.RuntimeException("不支持"+t.getClass().getName()+"使用该方法");
		}
	}
	
	
	public <T> String updScre(T t,Integer id)throws Exception {
		if (RptBase.class.isAssignableFrom(t.getClass())) {//判断 要保存的实体bean是否继承了RptBase
			RptBase base=(RptBase)t;
			String screType=base.getScreType();
			String screTopic=base.getScreTopic();
			Integer inOrder=base.getInOrder();
			Object oldt=dao.get(t.getClass(), id);
			//将前台收到的值赋值到查询出的实体里
			if (oldt!=null) {
				Field[] newField=t.getClass().getDeclaredFields();
				for(int i=0;i<newField.length;i++) {
					newField[i].setAccessible(true);//设置可访问私有
					Object newvalue=newField[i].get(t);
					if(null!=newvalue) {
						Field oldField=oldt.getClass().getDeclaredField(newField[i].getName());
						oldField.setAccessible(true);
						oldField.set(oldt, newvalue);
					}
				}
				RptBase newBase=(RptBase)oldt;//强转
				//计算科研积分
				Integer expectedMark=standService.getStandardMark(screType,screTopic,inOrder,newBase.getBelongCycle());
				newBase.setScreTopic(screTopic);
				newBase.setScreType(screType);
				newBase.setInOrder(inOrder);
				newBase.setExpectedMark(expectedMark);
				dao.update(newBase);
			}else {
				throw new java.lang.RuntimeException("未找到记录"+t.getClass().getName()+",id="+id);
			}
			return "succ";
		}else {
			throw new java.lang.RuntimeException("不支持"+t.getClass().getName()+"使用该方法");
		}
	}

	/**
	 * 通用删除
	 * @param <T>
	 * @param t
	 * @param priKey 主键名称
	 * @throws Exception
	 */
	public <T> void delScre(T t,String priKey,String memo) throws Exception{
		int IdValue=commonDao.delScre(t,priKey);
		logDao.addReviewLog(FormatUtil.getEntityName(t.getClass().getName()), IdValue,"通过", "BApprove", "FApprove", memo);
	}
	
	/**
	 * 修改科研考核记录的状态-通用
	 * @param <T>
	 * @param t
	 * @param id
	 * @param action
	 * @param newStatus
	 * @param memo
	 * @throws Exception
	 */
	
	public <T> void updScreStatus(T t,int id,String action ,String newStatus,String memo) throws Exception{
		if (RptBase.class.isAssignableFrom(t.getClass())) {//判断 要保存的实体bean是否继承了RptBase
			Object oldt=dao.get(t.getClass(), id);
			//将前台收到的值赋值到查询出的实体里
			if (oldt!=null) {
				RptBase newBase=(RptBase)oldt;//强转
				String oldStatus=newBase.getStatus();
				newBase.setStatus(newStatus);
				dao.update(newBase);
				logDao.addReviewLog(FormatUtil.getEntityName(t.getClass().getName()), id,action, oldStatus, newStatus, memo);
			}else {
				throw new java.lang.RuntimeException("未找到记录"+t.getClass().getName()+",id="+id);
			}
		}else {
			throw new java.lang.RuntimeException("不支持"+t.getClass().getName()+"使用该方法");
		}
	}
	
	public <T> void passScreStatus(T t,int id,String newStatus,String memo) throws Exception{
		updScreStatus(t,id,"通过",newStatus,memo);
	}
	public <T> void refuseScreStatus(T t,int id,String newStatus,String memo) throws Exception{
		updScreStatus(t,id,"拒绝",newStatus,memo);
	}
	/**
	 * 更新是否推荐
	 * */
	public <T> void updScreRecommend(T t,int id,String recommend) throws Exception{
		if (RptBase.class.isAssignableFrom(t.getClass())) {//判断 要保存的实体bean是否继承了RptBase
			Object oldt=dao.get(t.getClass(), id);
			if (oldt!=null) {
				RptBase newBase=(RptBase)oldt;//强转
				newBase.setRecommend(recommend);
				dao.update(newBase);
			}else {
				throw new java.lang.RuntimeException("未找到记录"+t.getClass().getName()+",id="+id);
			}
		}else {
			throw new java.lang.RuntimeException("不支持"+t.getClass().getName()+"使用该方法");
		}
	}
		
	/**
	 * 更新记录状态
	 * @param <T>
	 * @param t
	 * @param priKey
	 * @param idValue
	 * @param oldStatus
	 * @param newStatus
	 * @param memo
	 * @throws Exception
	 */
	public <T> void passScreStatus(T t,String priKey,int idValue,String oldStatus,String newStatus,String memo) throws Exception{
		int res=commonDao.updScreStatus(t, priKey, idValue, newStatus);
		if(res==1) {
			logDao.addReviewLog(FormatUtil.getEntityName(t.getClass().getName()), idValue,"通过", oldStatus, newStatus, memo);
		}else {
			throw new java.lang.RuntimeException("未找到记录"+t.getClass().getName()+",priKey="+idValue);
		}
		
	}
	public <T> void refuseScreStatus(T t,String priKey,int idValue,String oldStatus,String newStatus,String memo) throws Exception{
		int res=commonDao.updScreStatus(t, priKey, idValue, newStatus);
		if(res==1) {
			logDao.addReviewLog(FormatUtil.getEntityName(t.getClass().getName()), idValue,"拒绝", oldStatus, newStatus, memo);
		}else {
			throw new java.lang.RuntimeException("未找到记录"+t.getClass().getName()+",priKey="+idValue);
		}
	}
	
	/**
	 * 获取审批记录
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public List<SysReviewLog> findLog(String tblName,Integer recordId)throws Exception{
		return logDao.findLog(tblName, recordId);
	}


	/**
	 * 导出excel ,XSSFWorkbook是操作Excel2007的版本，扩展名是.xlsx
	 * @param t
	 * @param cycleName
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public <T> Workbook fillExcelDataWithTemplate(T t,String cycleName) throws Exception{
		List<T> ls=getContentByCon(t);
		String excelTmpName= FormatUtil.getEntityName(t.getClass().getName());
		//1、获取模板文件
		String basePath = ResourceUtils.getURL("classpath:").getPath();
		InputStream ExcelFileToRead = new FileInputStream(basePath + "/static/xlsTemplates/"+excelTmpName+ Constant.EXCEL_EXTENSION);


		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		XSSFSheet sheet = wb.getSheetAt(0);

		//2、创建标题
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		XSSFRow row1 = (XSSFRow) sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		XSSFCell cell = row1.createCell(0);
		String titleType=CacheData.getDictValByKey(excelTmpName, "统计表标题");
		//标题的单元格样式
		CellStyle style=wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		Font font=wb.createFont() ;
		font.setFontHeight((short)300);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		cell.setCellStyle(style);
		cell.setCellValue("黑龙江财经学院"+cycleName+titleType+"成果统计表");

		//3、从第三行开始写入数据
		int rowIndex = 3;
		int i = 0;
		switch(excelTmpName) {
			case "RptBook":{
				List<RptBook> list=(List<RptBook>) ls;
				for (RptBook book : list) {
					XSSFRow row = sheet.createRow(rowIndex);
					rowIndex++;
					i++ ;
					row.createCell(0).setCellValue(i);//序号
					row.createCell(1).setCellValue(book.getStaffName());//姓名
					row.createCell(2).setCellValue(book.getStaffTitle());//职称
					row.createCell(3).setCellValue(book.getStaffParentDepart());//系部处
					row.createCell(4).setCellValue(book.getStaffDepart());//科室
					row.createCell(5).setCellValue(book.getBookName());//教材、专著名称
					row.createCell(6).setCellValue(book.getFirstEditor());//主编或第一主编
					row.createCell(7).setCellValue(book.getPress());//出版社
					row.createCell(8).setCellValue(DateUtil.formatDate(book.getPublishDate(), "yyyy-MM-dd"));//发表日期
				}
				break;
			}
			case "RptAcademic":{
				List<RptAcademic> list=(List<RptAcademic>) ls;
				for (RptAcademic academic : list) {
					Row row = sheet.createRow(rowIndex);
					rowIndex++;
					i++ ;
					row.createCell(0).setCellValue(i);//序号
					row.createCell(1).setCellValue(academic.getStaffName());//姓名
					row.createCell(2).setCellValue(academic.getStaffTitle());//职称
					row.createCell(3).setCellValue(academic.getStaffParentDepart());//系部处
					row.createCell(4).setCellValue(academic.getStaffDepart());//科室
					row.createCell(5).setCellValue(academic.getAcademicTopic());//报告主题
					row.createCell(6).setCellValue(academic.getAcademicDate());//主讲日期
					row.createCell(7).setCellValue(academic.getAcademicMemo());//报告简介
					row.createCell(8).setCellValue(academic.getScreTopic());//学术类型
				}
				break;
			}
			case "RptAchievement":{
				List<RptAchievement> list=(List<RptAchievement>) ls;
				for (RptAchievement achievement : list) {
					Row row = sheet.createRow(rowIndex);
					rowIndex++;
					i++ ;
					row.createCell(0).setCellValue(i);//序号
					row.createCell(1).setCellValue(achievement.getStaffName());//姓名
					row.createCell(2).setCellValue(achievement.getStaffTitle());//职称
					row.createCell(3).setCellValue(achievement.getStaffParentDepart());//系部处
					row.createCell(4).setCellValue(achievement.getStaffDepart());//科室
					row.createCell(5).setCellValue(achievement.getAchievementName());//成果名称
					row.createCell(6).setCellValue(achievement.getAchievementType());//成果类型
					row.createCell(7).setCellValue(achievement.getAwardName());//获奖名称
					row.createCell(8).setCellValue(achievement.getScreTopic());//获奖等级
					row.createCell(9).setCellValue(achievement.getAwardDepart());//颁奖部门
					row.createCell(10).setCellValue(achievement.getFirstPerson());//第一完成人
				}
				break;
			}
			case "RptPaper":{
				List<RptPaper> list=(List<RptPaper>) ls;
				for (RptPaper paper : list) {
					Row row = sheet.createRow(rowIndex);
					rowIndex++;
					i++ ;
					row.createCell(0).setCellValue(i);//序号
					row.createCell(1).setCellValue(paper.getStaffName());//姓名
					row.createCell(2).setCellValue(paper.getStaffTitle());//职称
					row.createCell(3).setCellValue(paper.getStaffParentDepart());//系部处
					row.createCell(4).setCellValue(paper.getStaffDepart());//科室
					row.createCell(5).setCellValue(paper.getPaperThesis());//论文题目
					row.createCell(6).setCellValue(paper.getPaperJournal());//期刊名称
					row.createCell(7).setCellValue(paper.getScreTopic());//期刊类型
					row.createCell(8).setCellValue(paper.getPaperSponsor());//主办单位
					row.createCell(9).setCellValue(DateUtil.formatDate(paper.getPublishDate(), "yyyy-MM-dd"));//发表日期
					row.createCell(10).setCellValue(paper.getExpectedMark());//可得积分
					row.createCell(11).setCellValue(paper.getFileCount());//附件数
				}
				break;
			}
			case "RptPatent":{
				List<RptPatent> list=(List<RptPatent>) ls;
				for (RptPatent patent : list) {
					Row row = sheet.createRow(rowIndex);
					rowIndex++;
					i++ ;
					row.createCell(0).setCellValue(i);//序号
					row.createCell(1).setCellValue(patent.getStaffName());//姓名
					row.createCell(2).setCellValue(patent.getStaffTitle());//职称
					row.createCell(3).setCellValue(patent.getStaffParentDepart());//系部处
					row.createCell(4).setCellValue(patent.getStaffDepart());//科室
					row.createCell(5).setCellValue(patent.getPatentName());//专利名称
					row.createCell(6).setCellValue(patent.getPatentNumber());//专利证号
					row.createCell(7).setCellValue(patent.getScreTopic());//专利类型
					row.createCell(8).setCellValue(patent.getPatentCompany());//专利鉴定单位
					row.createCell(9).setCellValue(patent.getPatentPerson());//完成人
				}
				break;
			}
			case "RptScientific":{
				List<RptScientific> list=(List<RptScientific>) ls;
				for (RptScientific scientific : list) {
					Row row = sheet.createRow(rowIndex);
					rowIndex++;
					i++ ;
					row.createCell(0).setCellValue(i);//序号
					row.createCell(1).setCellValue(scientific.getStaffName());//姓名
					row.createCell(2).setCellValue(scientific.getStaffTitle());//职称
					row.createCell(3).setCellValue(scientific.getStaffParentDepart());//系部处
					row.createCell(4).setCellValue(scientific.getStaffDepart());//科室
					row.createCell(5).setCellValue(scientific.getScreType());//项目类别
					row.createCell(6).setCellValue(scientific.getScieName());//项目名称
					row.createCell(7).setCellValue(scientific.getScieDepart());//项目审批部门
					row.createCell(8).setCellValue(scientific.getScieLeader());//负责人
					row.createCell(9).setCellValue(DateUtil.formatDate(scientific.getScieStartDate(), "yyyy-MM-dd"));//立项时间
					row.createCell(10).setCellValue(DateUtil.formatDate(scientific.getScieCloseDate(), "yyyy-MM-dd"));//结题时间
					row.createCell(11).setCellValue(scientific.getScieOk());//是否结题
				}
				break;
			}
			default:
				throw new java.lang.RuntimeException("不识别的表类型："+excelTmpName);
		}
		setCellStyle(wb, sheet,3, rowIndex-1);
		return wb;

	}

	/**
	 * 给单元格统计加样式
	 * @param wb
	 * @param sheet
	 * @param startRow 开始行
	 * @param lastRow  最后一行
	 */
	public void setCellStyle(Workbook wb, Sheet sheet, int startRow, int lastRow){
		//设置内容单元格样式,加边框
		CellStyle cellStyle =wb.createCellStyle();
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);

		for (int i=startRow; i<=lastRow; i++){
		    Row rowI = sheet.getRow(i);
			for (Iterator<Cell> cellIterator = rowI.cellIterator(); cellIterator.hasNext();) {
				//得到该单元格
				Cell cell = cellIterator.next();
				cell.setCellStyle(cellStyle);
			}
		}

	}
	public static void main(String[] args) {
		String ss="edu.hfu.scre.entity.RptPaper";
		System.out.println(ss.substring(ss.lastIndexOf('.')+1));
	}
	
	public List<Map<String,Object>> getScreStatistics(ViewscreAchieve view)throws Exception{
		
		return commonDao.getScreStatistics(view,1,6);
	}
	public Integer findViewscreAchieveCount(ViewscreAchieve view) throws Exception {
		return commonDao.findViewscreAchieveCount(view);
	}
 public List<Map<String,Object>> getScreStatisticsall(ViewscreAchieve view,int pageNo,int maxResults)throws Exception{
		
		return commonDao.getScreStatistics(view,pageNo,maxResults);
	}
}
