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
import edu.hfu.scre.entity.RptScientific;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.review.ScientificService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.util.FormatUtil;
import edu.hfu.scre.util.ResponseUtil;

@RestController
@RequestMapping(value="/statistics")
public class ScientificStatisticsAction {
		private static final Logger LOG = LoggerFactory.getLogger(ScientificStatisticsAction.class);
		@Resource
		DepartService departService;
		@Autowired
		private HttpSession session;
		@Resource
		CommonScreService commonService;
		@Resource(name = "scientificService")
		ScientificService scientificService;

		/**
		 * 进入统计界面
		 * @return
		 */
		@RequestMapping(value="/initScientificstatistics", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/initScientificCollege','000300')")
	    public ModelAndView initScientificCollege(){
			//列表
			List<SysDepart> departs=departService.getAllDepartOne();
			ModelAndView mod= new ModelAndView("/statistics/scientificStatistics.btl");
			mod.addObject("departs", departs);
			return mod;
		}

	
		/**
		 * 查询院已通过的科研课题
		 * @return
		 */
		@RequestMapping(value="/queryStatisticsScientific", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/queryScientificCourtyard','000300000')")
		public Map<String,Object> queryScientificCollege(RptScientific scientific,Integer pageNo,Integer maxResults) {
			Map<String,Object> rtnMap=new HashMap<>();
			try {
				SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
				if (null==cycle) {
					rtnMap.put("mess", "未获取到科研周期！");
					rtnMap.put("total", 0);
					rtnMap.put("rows", null);
				}else {
					scientific.setStatus("EApprove");
					scientific.setBelongCycle(cycle.getCycleName());
					List<RptScientific> rows=commonService.getContentByCon(scientific, pageNo, maxResults);
					Integer total=commonService.getCountByCon(scientific);
					String mess="succ";
					rtnMap.put("rows", rows);
					rtnMap.put("total", total);
					rtnMap.put("mess", mess);
				}
			} catch (Exception e) {
				e.printStackTrace();
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

	/**
	 * @param response
	 * @param request
	 * @param scientific
	 * @param pageNo
	 * @param maxResults
	 * @return
	 */
	@RequestMapping(value = "/scientificExcel", method = {RequestMethod.POST,RequestMethod.GET})
	@PreAuthorize("hasPermission('/scientificExcel','000300001')")
	public String scientificExcel(HttpServletResponse response, HttpServletRequest request,RptScientific scientific) {
		//String webPath = request.getServletContext().getRealPath("/");
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			String cycleName="";
			if (null==cycle) {
				throw new  java.lang.RuntimeException("未找到默认的科研周期");
			}else {

				scientific.setStatus("EApprove");
				scientific.setBelongCycle(cycle.getCycleName());
				cycleName=cycle.getCycleName()+"("+cycle.getBeginDate()+"至"+cycle.getEndDate()+"年度)";
			}
			Workbook wb = commonService.fillExcelDataWithTemplate(scientific, cycleName);//指定模板路径
			String exportName=FormatUtil.getEntityName(scientific.getClass().getName())+FormatUtil.formatDateToStr(new Date(), "yyyyMMddHHmmss");
			ResponseUtil.export(response, wb, exportName+ Constant.EXCEL_EXTENSION);//指定文件名称
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value="/updscientificRend", method= {RequestMethod.GET,RequestMethod.POST})
	public String updScientificRend(RptScientific scientific,Integer scieId,String recommend) {
		LOG.debug("ScientificId:"+scientific.getScieId());
	    	String	mess="";
		try {
			commonService.updScreRecommend(scientific, scieId,recommend);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
		
	}

