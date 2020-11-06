package edu.hfu.scre.action.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.hibernate.exception.ConstraintViolationException;

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

import edu.hfu.scre.entity.RptPatent;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.DictionaryService;

@RestController
@RequestMapping(value="/review")
public class PatentAction {
	private static final Logger LOG = LoggerFactory.getLogger(PatentAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	@Resource
	private DictionaryService dictionaryService;
	
	/**
	 * 专利成果
	 * @return
	 */
	@RequestMapping(value="/initPatent", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initPatent','000003')")
	public ModelAndView initPatent(){
		ModelAndView mod= new ModelAndView("/review/patent.btl");
		List<SysDictionary> ls=dictionaryService.getDictonaryByType("专利成果");
		mod.addObject("dicts", ls); 
		return mod;
	} 
	
	/**
	 * 查询专利成果
	 * @return
	 */
	@RequestMapping(value="/queryPatent", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryPatent','000003000')")
	public Map<String,Object> queryPatent(RptPatent patent,Integer pageNo,Integer maxResults) {
		LOG.debug("patentName:"+patent.getPatentName());
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				patent.setStaffName(user.getStaffName());
				patent.setStaffTitle(user.getStaffTitle());
				patent.setStaffParentDepart(user.getStaffParentDepart());
				patent.setStaffDepart(user.getStaffDepart());
				patent.setUserCode(user.getUserCode());
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
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
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}
	/**
	 * 增加专利成果
	 * @return
	 */
	@RequestMapping(value="/savePatent", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/savePatent','000003001')")
	public String  savePatent(RptPatent patent) {
		LOG.debug("getPatentSponsor:"+patent.getPatentCompany());
		LOG.debug("getPatentName:"+patent.getPatentName());
		String mess="";
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				mess= "未获取到科研周期！";
			}else {
				patent.setBelongCycle(cycle.getCycleName());
				mess= commonService.saveScre(patent);
			}
		} catch (ConstraintViolationException con){
		      con.printStackTrace();
		      mess="专利名称重复，请修正!";
		}catch (Exception e) {
			e.printStackTrace();
			mess =e.toString().replace("java.lang.RuntimeException:", "");
		}
		return mess;
	}
	/**
	 * 修改专利成果
	 * @return
	 */
	@RequestMapping(value="/updPatent", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/updPatent','000003002')")
	public String updPatent(RptPatent patent) {
		LOG.debug("patentId:"+patent.getPatentId());
		String mess="";
		try {
			mess=commonService.updScre(patent, patent.getPatentId());
		} catch (Exception e) {
			mess=e.toString();
			if (mess.indexOf("constraint")>-1) {
				mess="专利名称重复，请修正!";
			}else {
				e.printStackTrace();
				mess =e.toString().replace("java.lang.RuntimeException:","");
			}
		}
		return mess;
	}
	/**
	 * 删除
	 * @param paperId
	 * @param memo
	 * @return
	 */
	@RequestMapping(value="/delPatent", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delPatent','000003006')")
	public Map<String,Object> delPaper(Integer patentId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			RptPatent patent=new RptPatent();
			patent.setPatentId(patentId);
			commonService.delScre(patent,"patentId", memo);
			String mess="succ";
			rtnMap.put("mess", mess);
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}
	/**
	 * 提交作品
	 * @param paperId
	 * @param memo
	 * @return
	 */
	@RequestMapping(value="/submitPatent", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/submitPatent','000003003')")
	public Map<String, Object> submitPatent(Integer patentId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			commonService.passScreStatus(new RptPatent(), "patentId", patentId, "BApprove", "CApprove", memo);
			String mess="succ";
			rtnMap.put("mess", mess);
		} catch (Exception e) {
			e.printStackTrace();
			String mess=e.toString();
			rtnMap.put("mess", mess);
		}
		return rtnMap;
	}
	
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
}
