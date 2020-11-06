package edu.hfu.scre.action.statistics;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.action.review.PaperAction;
import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.DictionaryService;
import edu.hfu.scre.util.DateUtil;
import edu.hfu.scre.util.FormatUtil;
import edu.hfu.scre.util.ResponseUtil;
@RestController
@RequestMapping(value="/statistics")
public class PaperStatisticsAction {
	private static final Logger LOG = LoggerFactory.getLogger(PaperAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	@Resource
	DepartService departService;
	/**
	 * 进入统计界面
	 * @return
	 */
	@RequestMapping(value="/initPaperStatistics", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initPaperStatistics','000302')")
    public ModelAndView initPaperStatistics(){
		//列表
		List<SysDepart> departs=departService.getAllDepartOne();
		ModelAndView mod= new ModelAndView("/statistics/paperStatistics.btl");
		mod.addObject("departs", departs);
		return mod;
	}
	
	/**
	 * 查询本院所有人已提交的
	 * @param scie
	 * @param pageNo
	 * @param maxResults
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/queryPaperStatistics", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryPaperStatistics','000302000')")
	public Map<String,Object> queryPaperStatistics(RptPaper paper,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
	try {
		SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
		if (null==cycle) {
			rtnMap.put("mess", "未获取到科研周期！");
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
		}else {
			paper.setStatus("EApprove");//设计查询状态条件
			paper.setBelongCycle(cycle.getCycleName());
			List<RptPaper> rows=commonService.getContentByCon(paper, pageNo, maxResults);
			Integer total=commonService.getCountByCon(paper);
			String mess="succ";
			rtnMap.put("total", total);
			rtnMap.put("rows", rows);
			rtnMap.put("mess", mess);
		}
	} catch (Exception e) {
		e.printStackTrace();
		LOG.error(e.getMessage());
		rtnMap.put("total", 0);
		rtnMap.put("rows", null);
		rtnMap.put("mess", e.toString());
	}
	return rtnMap;
	}
	/**
	 *导出 excel 使用我们的模板导出 
	 *
	 */
	@RequestMapping(value = "/paperExcel", method = {RequestMethod.POST,RequestMethod.GET})
	@PreAuthorize("hasPermission('/paperExcel','000302001')")
	public String paperExcel(HttpServletResponse response, HttpServletRequest request,  RptPaper paper) {
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			String cycleName="";
			if (null==cycle) {
				throw new  java.lang.RuntimeException("未找到默认的科研周期");
			}else {
				paper.setStatus("EApprove");
				paper.setBelongCycle(cycle.getCycleName());
				cycleName=cycle.getCycleName()+"("+cycle.getBeginDate()+"至"+cycle.getEndDate()+"年度)";
			}
			Workbook wb = commonService.fillExcelDataWithTemplate(paper, cycleName);
			String exportName=FormatUtil.getEntityName(paper.getClass().getName())+FormatUtil.formatDateToStr(new Date(), "yyyyMMddHHmmss");
			ResponseUtil.export(response, wb, exportName+".Constant.EXCEL_EXTENSION)");//指定文件名称
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *是否推荐
	 */
	@RequestMapping(value="/updPaperRend", method= {RequestMethod.GET,RequestMethod.POST})
	public String updPaperRend(RptPaper Paper,Integer paperId,String recommend) {
		LOG.debug("PaperId:"+Paper.getPaperId());
		 String mess="";
		try {
			commonService.updScreRecommend(Paper, paperId,recommend);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
}
