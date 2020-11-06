package edu.hfu.scre.action.statistics;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.statistics.StatisticsService;
import edu.hfu.scre.util.FormatUtil;

@RestController
@RequestMapping(value="/statistics")
public class ScreDepartStatisticsAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScreDepartStatisticsAction.class);
	@Autowired
	private HttpSession session;
	
	@Resource
	DepartService departService;
	@Resource
	StatisticsService statisticsService;
	/**
	 *  进入系统计
	 * @return
	 */
	@RequestMapping(value="/initScreDepartStatistics", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView initScreDepartStatistics() {
		ModelAndView mod= new ModelAndView("/statistics/screDepartStatistics.btl");
		try {
			//部门名称
			String departName="";
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				departName=user.getStaffParentDepart();
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null!=cycle) {
				//计算入职日期为考核周期结束前12个月，参与考核
				Calendar cycleDate = Calendar.getInstance();
				cycleDate.setTime(FormatUtil.strToDate(cycle.getEndDate(), "yyyy-MM-dd"));
				cycleDate.add(Calendar.MONTH, -12);
				Map<String, Object> depart=departService.getDepartInfo(departName, FormatUtil.formatDateToStr(cycleDate.getTime(),"yyyy-MM-dd"));
				if (!"succ".equals(String.valueOf(depart.get("mess")))){
					LOG.error(String.valueOf(depart.get("mess")));
				}
				mod.addObject("depart", depart);
				Integer screStaff =Integer.parseInt(String.valueOf(depart.get("screStaff")));
				Map<String,Object> staticsMap=statisticsService.queryDepartScreInfo(departName,cycle);
				Integer reachCount=Integer.parseInt(String.valueOf(staticsMap.get("reachCount")));
				staticsMap.put("percent", FormatUtil.percentByNum(reachCount, screStaff));
				mod.addObject("staticsMap", staticsMap);
			}else {
				throw new java.lang.RuntimeException("无法获取科研周期");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mod;
	}
	/**
	 * 系科研作品汇总
	 * @return
	 */
	@RequestMapping(value="/getScreStatisByClassType", method= {RequestMethod.POST})
	public List<Map<String,Object>> getScreStatisByClassType() {
		try {
			//部门名称
			String departName="";
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				departName=user.getStaffParentDepart();
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (cycle!=null) {
				String[] status= {"DApprove","EApprove"};
				return statisticsService.getScreStatisByClassType(departName,cycle.getCycleName(),status);
			}else {
				throw new java.lang.RuntimeException("无法获取科研周期");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	
	
}
