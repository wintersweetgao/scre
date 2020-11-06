package edu.hfu.scre.action.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.statistics.StatisticsService;
import edu.hfu.scre.service.sysset.ScreCycleService;
import edu.hfu.scre.util.CacheData;

@RestController
@RequestMapping(value="/statistics")
public class ScreCollegeStatisticsAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScreCollegeStatisticsAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	StatisticsService statisticsService;
	@Resource
	ScreCycleService screCycleService;
	/**
	 *  进入院统计
	 * @return
	 */
	@RequestMapping(value="/initScreCollegeStatistics", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView initScreCollegeStatistics() {
		ModelAndView mod= new ModelAndView("/statistics/screCollegeStatistics.btl");
		SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
		if (cycle!=null) {
			String cycleName=cycle.getCycleName()+"("+cycle.getBeginDate()+"至"+cycle.getEndDate()+"年度)";
			mod.addObject("cycleName", cycleName);
		}else {
			mod.addObject("cycleName", "未能获取科研周期");
		}
		
		return mod;
	}
	@RequestMapping(value="/getScreStatisByCollegeClassType", method= {RequestMethod.GET,RequestMethod.POST})
	public List<Map<String,Object>> getScreStatisByCollegeClassType(){
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (cycle!=null) {
				String[] status= {"EApprove"};
				return statisticsService.getScreStatisByClassType(null,cycle.getCycleName(),status);
			}else {
				throw new java.lang.RuntimeException("无法获取科研周期");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 达标百分比
	 * @return
	 */
	@RequestMapping(value="/getScreStatisByCollegePercent", method= {RequestMethod.GET,RequestMethod.POST})
	public Map<String,Integer> getScreStatisByCollegePercent(){
		
		try {
			Map<String,Integer> map=new HashMap<>();
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (cycle!=null) {
				//获取参与达标的人
				int screCount=statisticsService.getAllScreStaff(cycle);
				//完成达标的人数
				int reachCount=statisticsService.getAllFinScreStaff(cycle);
				map.put("reachCount", reachCount);
				map.put("screCount", screCount);
				return map;
			}else {
				throw new java.lang.RuntimeException("无法获取科研周期");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/getScreStatisByCollegeCycle", method= {RequestMethod.GET,RequestMethod.POST})
	public Map<String,Object> getScreStatisByCollegeCycle(){
		try {
			LOG.debug("getScreStatisByCollegeCycle...");
			Map<String,Object> map=new HashMap<>();
			List<SysScreCycle> cycles=screCycleService.getLastFiveCycle();
			map.put("cycles", cycles);
			Map<String,List<Map<String,Object>>>  screDatas=statisticsService.getScreStatisByCycle(cycles.size());
			map.put("screDatas", screDatas);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/getScreStatisByRptCountCollege", method= {RequestMethod.GET,RequestMethod.POST})
	public List<Map<String,Object>> getScreStatisByRptCountCollege(){
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (cycle!=null) {
				return statisticsService.getScreStatisByDepart( cycle.getCycleName());
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
