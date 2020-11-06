package edu.hfu.scre.action.sysset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.SysStaffMarkStandard;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.DictionaryService;
import edu.hfu.scre.service.sysset.ScreCycleService;
import edu.hfu.scre.service.sysset.StaffTopicStandService;

@RestController
@RequestMapping(path = "/sysset")
public class StaffTopicStandAction {
	private static final Logger LOG = LoggerFactory.getLogger(StaffTopicStandAction.class);
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private StaffTopicStandService staffTopicStandService;
	@Resource
	private ScreCycleService screCycleService;

	@RequestMapping(value="/initStaffTopic", method= {RequestMethod.GET,RequestMethod.POST})
	//返回路径和页面用ModelAndView
	@PreAuthorize("hasPermission('/initStaffTopic','000501')")//访问页面权限
	public ModelAndView initStaffTopic(){
		
		LOG.debug("进入/sysset/staffTopicStand.btl");
		ModelAndView mod= new ModelAndView("/sysset/staffTopicStand.btl");
		try {
			List<SysDictionary> ls=dictionaryService.getDictonaryByType("职称分类");;
			mod.addObject("dicts", ls);
			
			List<SysScreCycle> cycles=screCycleService.getAllScreCycle();
			mod.addObject("cycles", cycles);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mod;
	} 
	//查询
	@RequestMapping(value="/queryStaffTopicStand", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryStaffTopicStand','000501000')")//查询权限
	public Map<String,Object> queryStaffTopicStand(SysStaffMarkStandard stand,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			List<SysStaffMarkStandard> rows=staffTopicStandService.queryStaffTopicStand(stand,pageNo,maxResults);
			Integer total=staffTopicStandService.queryStaffTopicStandCount(stand);
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
	}//增加
	@RequestMapping(value="/savestaffTopicStand", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryStaffTopicStand','000501001')")//增加权限
	public String savestaffTopicStand(SysStaffMarkStandard stand)  {
		String mess="";
		try {
			LOG.debug("进入savestaffTopicStand "+stand.getStaffTitle());
			staffTopicStandService.saveStaffTopicStand(stand);
			mess="succ";
		}catch (ConstraintViolationException con){
			con.printStackTrace();
			mess="重复添加";
		}
		catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}	//删除
	@RequestMapping(value="/delstaffTopicStand", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delstaffTopicStand','000501003')")//删除权限
	public String delstaffTopicStand(Integer standardId)  {
		String mess="";
		try {
			LOG.debug("进入delstaffTopicStand,id= "+standardId);
			staffTopicStandService.delStaffTopicStand(standardId);;
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}//修改
	@RequestMapping(value="/updstaffTopicStand", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/updstaffTopicStand','000501002')")//修改权限
	public String updstaffTopicStand(SysStaffMarkStandard staffTitle)  {
		String mess="";
		try {
			LOG.debug("进入updstaffTopicStand,id= "+staffTitle.getStaffTitle());
			staffTopicStandService.updStaffTopicStand(staffTitle);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
}
