package edu.hfu.scre.action.sysset;


import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysScreStandard;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.sysset.DictionaryService;
import edu.hfu.scre.service.sysset.ScreCycleService;
import edu.hfu.scre.service.sysset.ScreStandardService;

@RestController
@RequestMapping(path = "/sysset")
public class ScreStandardAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScreStandardAction.class);
	
	@Resource
	private ScreStandardService screStandardService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private ScreCycleService screCycleService;
	
	@RequestMapping(value="/initScreStandard", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initScreStandard','000502')")//访问权限
	//返回路径和页面用ModelAndView
	public ModelAndView initStandard(){
		LOG.debug("进入screStandard.btl");
		ModelAndView mod= new ModelAndView("/sysset/screStandard.btl");
		try {
		List<SysDictionary> ls=dictionaryService.getDictonaryByType("科研达标类型");
		mod.addObject("dicts", ls);
		List<SysScreCycle> cycles=screCycleService.getAllScreCycle();
		mod.addObject("cycles", cycles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mod;
	}
	
	@RequestMapping(value="/queryScreStand", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryScreStand','000502000')")//查询权限
	public Map<String,Object> queryScreStand(SysScreStandard stand,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			List<SysScreStandard> rows=screStandardService.queryAllScreStandard(stand,pageNo,maxResults);
			Integer total=screStandardService.getstandCount(stand);
			LOG.debug("评分标准数据："+total);
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
	@RequestMapping(value="/saveScreStandard", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/saveScreStandard','000502001')")//增加权限
	public String saveScreStandard(SysScreStandard scre)  {
		String mess="";
		try {
			LOG.debug("进入saveScreStandard "+scre.getScreType());
			screStandardService.saveScreStandard(scre);
			mess="succ";
		}catch (ConstraintViolationException con){
			con.printStackTrace();
			mess="重复添加";
		}catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			mess ="重复添加！";
		}catch (Exception e) {
			e.printStackTrace();
			mess =e.toString();
		}
		return mess;
	}
	@RequestMapping(value="/delScreStandard", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delScreStandard','000502003')")//删除权限
	public String delScreStandard(Integer standardId)  {
		String mess="";
		try {
			LOG.debug("进入delScreStandard,id= "+standardId);
			screStandardService.delScreStandard(standardId);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
	@RequestMapping(value="/updScreStandard", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/updScreStandard','000502002')")//修改权限
	public String updScreStandard(SysScreStandard scre)  {
		String mess="";
		try {
			LOG.debug("进入updScreStandard,id= "+scre.getStandardId());
			screStandardService.updScreStandard(scre);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
	
}
