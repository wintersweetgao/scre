package edu.hfu.scre.action.statistics;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.hfu.scre.util.Constant;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.RptPatent;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.util.FormatUtil;
import edu.hfu.scre.util.ResponseUtil;

@RestController
@RequestMapping(value="/statistics")
public class PatentStatisticsAction {
	private static final Logger LOG = LoggerFactory.getLogger(PatentStatisticsAction.class);
	@Resource
	DepartService departService;
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	
	//进入EApprove界面
		@RequestMapping(value="/initPatentStatistics", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/initPatentStatistics','000303')")
	    public ModelAndView initPatentStatistics(){
			//列表
			List<SysDepart> departs=departService.getAllDepartOne();
			ModelAndView mod= new ModelAndView("/statistics/patentStatistics.btl");
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
		@RequestMapping(value="/queryPatentStatistics", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/queryPatentStatistics','000303000')")
		public Map<String,Object> queryPatentCollege(RptPatent patent,Integer pageNo,Integer maxResults){
			Map<String,Object> rtnMap=new HashMap<>();
			try {
				SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
				if (null==cycle) {
					rtnMap.put("mess", "未获取到科研周期！");
					rtnMap.put("total", 0);
					rtnMap.put("rows", null);
				}else {
					patent.setStatus("EApprove");
					patent.setBelongCycle(cycle.getCycleName());
					List<RptPatent> rows=commonService.getContentByCon(patent, pageNo, maxResults);
					Integer total=commonService.getCountByCon(patent);
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

	@RequestMapping(value = "/patentExcel", method = {RequestMethod.POST,RequestMethod.GET})
	@PreAuthorize("hasPermission('/patentExcel','000303001')")
	public String patentExcel(HttpServletResponse response, HttpServletRequest request, RptPatent patent) {
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			String cycleName="";
			if (null==cycle) {
				throw new  java.lang.RuntimeException("未找到默认的科研周期");
			}else {
				patent.setStatus("EApprove");
				patent.setBelongCycle(cycle.getCycleName());
				cycleName=cycle.getCycleName()+"("+cycle.getBeginDate()+"至"+cycle.getEndDate()+"年度)";
			}
			Workbook wb = commonService.fillExcelDataWithTemplate(patent, cycleName);
			String exportName=FormatUtil.getEntityName(patent.getClass().getName())+FormatUtil.formatDateToStr(new Date(), "yyyyMMddHHmmss");
			ResponseUtil.export(response, wb, exportName+ Constant.EXCEL_EXTENSION);//指定文件名称
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *是否推荐
	 */
	@RequestMapping(value="/updPatentRend", method= {RequestMethod.GET,RequestMethod.POST})
	public String updPatentRend(RptPatent patent,Integer patentId,String recommend) {
		LOG.debug("PatentId:"+patent.getPatentId());
		String mess="";
		try {
			commonService.updScreRecommend(patent, patentId,recommend);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
}
