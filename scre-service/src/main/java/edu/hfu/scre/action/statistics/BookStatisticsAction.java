package edu.hfu.scre.action.statistics;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;

import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.util.ResponseUtil;
import edu.hfu.scre.util.FormatUtil;

@RestController
@RequestMapping(value="/statistics")
public class BookStatisticsAction {
	private static final Logger LOG = LoggerFactory.getLogger(BookStatisticsAction.class);
	@Resource
	DepartService departService;
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	/**
	 * 进入统计界面
	 * @return
	 */

	@RequestMapping(value="/initBookStatistics", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initBookStatistics','000301')")
    public ModelAndView initBookStatistics(){
		//列表
		List<SysDepart> departs=departService.getAllDepartOne();
		ModelAndView mod= new ModelAndView("/statistics/bookStatisics.btl");
		mod.addObject("departs", departs);
		LOG.debug("departs:"+departs);
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
	@RequestMapping(value="/queryBookstatistics", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryBookstatistics','000301000')")
	public Map<String,Object> queryBookstatistics(RptBook book,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				book.setStatus("EApprove");
				book.setBelongCycle(cycle.getCycleName());
				List<RptBook> rows=commonService.getContentByCon(book, pageNo, maxResults);
				Integer total=commonService.getCountByCon(book);
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
	 *    /statistics/optexcel
	 */
	
	@RequestMapping(value = "/bookExcel", method = {RequestMethod.POST,RequestMethod.GET})
	@PreAuthorize("hasPermission('/bookExcel','000301001')")
	public String bookExcel(HttpServletResponse response, HttpServletRequest request, RptBook book) {
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			String cycleName="";
			if (null==cycle) {
				throw new  java.lang.RuntimeException("未找到默认的科研周期");
			}else {
				book.setStatus("EApprove");
				book.setBelongCycle(cycle.getCycleName());
				cycleName=cycle.getCycleName()+"("+cycle.getBeginDate()+"至"+cycle.getEndDate()+"年度)";
			}
			Workbook wb = commonService.fillExcelDataWithTemplate(book, cycleName);
			String exportName=FormatUtil.getEntityName(book.getClass().getName())+FormatUtil.formatDateToStr(new Date(), "yyyyMMddHHmmss");
			ResponseUtil.export(response, wb, exportName+".xls");//指定文件名称
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *是否推荐
	 */
	@RequestMapping(value="/updBookRend", method= {RequestMethod.GET,RequestMethod.POST})
	public String updBookRend(RptBook Book,Integer bookID,String recommend) {
		LOG.debug("BookID:"+Book.getBookID());
		String mess="";
		try {
			commonService.updScreRecommend(Book, bookID,recommend);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
}
