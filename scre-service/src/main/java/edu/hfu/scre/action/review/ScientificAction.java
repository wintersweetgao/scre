package edu.hfu.scre.action.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.sysset.DictionaryService;
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

import edu.hfu.scre.entity.RptScientific;
import edu.hfu.scre.entity.SysReviewLog;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.ScientificService;
import edu.hfu.scre.service.security.MyUserDetails;

/**
 * @author JLH
 * @date 2019/10/11 14:52
 *
 * 科研科题（提交）服务
 */

@RestController
@RequestMapping(value="/review")
public class ScientificAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScientificAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	@Resource
	private DictionaryService dictionaryService;


	/**
	 * 个人提交科研课题
	 * @return
	 */
	@RequestMapping(value="/initScientific", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initScientific','000000')")
	public ModelAndView initScientific(){
		ModelAndView mod= new ModelAndView("/review/scientific.btl");
		return mod;
	}
	/**
	 * 查询科研课题提交内容
	 * @return
	 */
	@RequestMapping(value="/queryScientific", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryScientific','000000000')")
	public Map<String,Object> queryScientific(RptScientific scie,Integer pageNo,Integer maxResults) {
		LOG.debug("scieId:"+scie.getScieId());
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				scie.setStaffName(user.getStaffName());
				scie.setStaffTitle(user.getStaffTitle());
				scie.setStaffParentDepart(user.getStaffParentDepart());
				scie.setStaffDepart(user.getStaffDepart());
				scie.setUserCode(user.getUserCode());
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				scie.setBelongCycle(cycle.getCycleName());
				List<RptScientific> rows=commonService.getContentByCon(scie, pageNo, maxResults);
				Integer total=commonService.getCountByCon(scie);
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


	@RequestMapping(value="/saveScientific", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/saveScientific','000000001')")
	public String  saveScientific(RptScientific scie) throws Exception {
		LOG.debug("scieName:"+scie.getScieName());
		LOG.debug("screType:"+scie.getScreType());
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				scie.setStaffName(user.getStaffName());
				scie.setStaffTitle(user.getStaffTitle());
				scie.setStaffParentDepart(user.getStaffParentDepart());
				scie.setStaffDepart(user.getStaffDepart());
				scie.setUserCode(user.getUserCode());
			}

			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				return "未获取到科研周期！";
			}else {
				scie.setBelongCycle(cycle.getCycleName());
				return commonService.saveScre(scie);
			}
		} catch (ConstraintViolationException c){
			c.printStackTrace();
			return "您提交的项目名称+参与人次序重复了！";
		}catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@RequestMapping(value="/updScientific", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/updScientific','000000002')")
	public String updScientific(RptScientific scie) {
		LOG.debug("scieId:"+scie.getScieId());
		String mess="";
		try {
			mess=commonService.updScre(scie,scie.getScieId());
		} catch (Exception e) {
			mess=e.toString();
			if (mess.indexOf("constraint")>-1) {
				mess="项目名称重复，请修正！";
			}else {
				e.printStackTrace();
			}
		}
		return mess;
	}
	@RequestMapping(value="/delScientific", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delScientific','000000006')")
	public Map<String,Object> delScientific(Integer scieId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			RptScientific sice=new RptScientific();
			sice.setScieId(scieId);
			commonService.delScre(sice,"scieId", memo);
			String mess="succ";
			rtnMap.put("mess", mess);
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}

	@RequestMapping(value="/subScientific", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/subScientific','000000003')")
	public Map<String, Object> submitScientific(Integer scieId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			commonService.passScreStatus(new RptScientific(), "scieId", scieId, "BApprove", "CApprove", memo);
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
