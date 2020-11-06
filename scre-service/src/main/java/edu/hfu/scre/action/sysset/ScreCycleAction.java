package edu.hfu.scre.action.sysset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.sysset.ScreCycleService;

@RestController
@RequestMapping(path = "/sysset")
public class ScreCycleAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScreCycleAction.class);
	int a;
	long b;
	float c;
	@Resource
	private ScreCycleService screCycleService;
	
	@RequestMapping(value="/initCycle", method= {RequestMethod.GET,RequestMethod.POST})
	//返回路径和页面用ModelAndView
	@PreAuthorize("hasPermission('/initCycle','000500')")
	public ModelAndView initCycle(){
		LOG.debug("进入/sysset/screCycle.btl");
		ModelAndView mod= new ModelAndView("/sysset/screCycle.btl");
		return mod;
	}
	
	@RequestMapping(value="/queryAllCycle", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryAllCycle','000500000')")
	public  Map<String,Object> queryAllCycle(){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			List<SysScreCycle> rows=screCycleService.getAllScreCycle();
			Integer total=rows.size();
			LOG.debug("获取全部科研周期数据："+total);
			String mess="succ";
			rtnMap.put("total", total);
			rtnMap.put("rows", rows);
			rtnMap.put("mess", mess);
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}
	@RequestMapping(value="/addNextCycle", method= {RequestMethod.POST})
	@PreAuthorize("hasPermission('/addNextCycle','000500000')")
	public String addNextCycle(SysScreCycle cycle) {
		String mess="";
		try {
			LOG.debug("保存科研周期："+cycle.getCycleName());
			screCycleService.addNextCycle(cycle);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
}
