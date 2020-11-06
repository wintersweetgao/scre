package edu.hfu.scre.action.review;


import java.util.Date;
import java.util.HashMap;
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

import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysPost;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.SysStaffMarkStandard;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.review.MyScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.security.PostService;
import edu.hfu.scre.service.security.StaffService;
import edu.hfu.scre.service.sysset.DictionaryService;
import edu.hfu.scre.service.sysset.StaffTopicStandService;
import edu.hfu.scre.util.MD5Util;

@RestController
@RequestMapping(value="/review")
public class MyScreAction {
	private static final Logger LOG = LoggerFactory.getLogger(MyScreAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	MyScreService myScreService;
	@Resource
	DepartService departService;
	@Resource
	PostService postService;
	@Resource
	StaffTopicStandService staffTopicStandService;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private StaffService staffService;
	
	@RequestMapping(value="/initMyScre", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView initMyScre() {
		ModelAndView mod= new ModelAndView("/review/myScre.btl");
		List<SysDictionary> ls=dictionaryService.getDictonaryByType("职称分类");
		List<SysDictionary> os=dictionaryService.getDictonaryByType("教育程度");
		
		List<SysPost> posts=postService.getAllSysPost();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal=auth.getPrincipal();
		//用户代码等
		if (principal != null && principal instanceof MyUserDetails) {
			MyUserDetails usd = (MyUserDetails) principal;
			SysStaff user=(SysStaff)usd.getCustomObj();
			Integer parentDepartId=user.getParentDepartId();
			
			mod.addObject("user", user);
			mod.addObject("dicts", ls); 
		    mod.addObject("dicto", os);
		    mod.addObject("posts",posts);
		    LOG.debug("职务内容："+posts+"departid:"+parentDepartId);
		}
		return mod;
	}
	@RequestMapping(value="/getDepartOne", method= {RequestMethod.POST})
	public List<SysDepart> getDepartOne(){
		try {
			List<SysDepart> departs=departService.getAllDepartOne();
			return departs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/getDepartTwo", method= {RequestMethod.POST})
	public List<SysDepart> getDepartTwo(Integer parentDepartId){
		try {
			List<SysDepart> departwo=departService.getAllDepartTwo(parentDepartId);
			return departwo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/getScreCountByCode", method= {RequestMethod.POST})
	public Map<String,Object> getScreCountByCode(){
		Map<String,Object> map=new HashMap<>();
		try {
			
			String userCode="";
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			//用户代码等
			String staffTitle="";
			Date entryDate=null;
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				userCode=user.getUserCode();
				staffTitle=user.getStaffTitle();
				entryDate=user.getEntryDate();
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null!=cycle) {
				String belongCycle=cycle.getCycleName();
				//预计积分
				List<Map<String,Object>>  expectedMarks=myScreService.getScreCountByCode(userCode, belongCycle);
				map.put("expectedMarks", expectedMarks);
				//实际积分
				List<Map<String,Object>>  approveMarks=myScreService.getScreApproveCountByCode(userCode, belongCycle);
				Integer approveMarkCount=0;
				for(Map<String,Object> app:approveMarks) {
					approveMarkCount+=Integer.parseInt(String.valueOf(app.get("expectedMark")).equals("null")?"0":String.valueOf(app.get("expectedMark")));
				}
				map.put("approveMarkCount", approveMarkCount);

				//积分标准
				SysStaffMarkStandard stand=staffTopicStandService.queryStaffTopicStand(staffTitle,belongCycle);
 			    if(stand==null){
					map.put("markStand", "");
				}else{
					Integer standMark = myScreService.getStandMark(entryDate,cycle,stand.getMarkStandard());
					map.put("markStand", standMark);
				}
				
			}else {
				throw new java.lang.RuntimeException("无法获取科研周期");
			}
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value="/updMyPass", method= {RequestMethod.POST})
	public String updMyPass(String myscre_password_old,String myscre_password_new) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal=auth.getPrincipal();
		if (principal != null && principal instanceof MyUserDetails) {
			MyUserDetails usd = (MyUserDetails) principal;
			SysStaff user=(SysStaff)usd.getCustomObj();
			if (!user.getUserPass().equals(MD5Util.string2MD5(myscre_password_old))) {
				return "用户原密码错误！";
			}else {
				Integer rtn=staffService.updUserPassword(user.getUserCode(), myscre_password_new);
				if (rtn==1) {
					return "success";
				}else {
					return "修改失败，未找到用户："+user.getUserCode();
				}
			}
		}
		return "登录超时,请重新登录";
	}
	/**
	 * 修改人员内容
	 * @return
	 */	
	@RequestMapping(value="/updUserByCode", method= {RequestMethod.GET,RequestMethod.POST})
	public String updUserByCode(SysStaff staff,String postIds) {
		System.out.println("departId:"+staff.getDepartId());
		System.out.println("postIds:"+postIds);
		System.out.println("staff:"+staff.getStaffName());
		System.out.println("staff:"+staff.getStaffEducation());
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				staff.setStaffId(user.getStaffId());
				return staffService.updUserByCode(staff, postIds);
			}else {
				return "登录超时！";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "succ";
	}
	
}
