package edu.hfu.scre.action.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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

import com.hanb.filterJson.annotation.PowerJsonFilter;
import com.hanb.filterJson.annotation.PowerJsonFilters;

import edu.hfu.scre.entity.RptScientific;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.ScientificService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;

/**
 * @author DJ
 * @date 2019/10/14 10:52
 *
 * 科研课题-DApprove
 */

@RestController
@RequestMapping(value="/review")
public class ScientificApproveAction {
	private static final Logger LOG = LoggerFactory.getLogger(ScientificApproveAction.class);
	
	@Resource(name = "scientificService")
	ScientificService scientificService;
	@Resource
	DepartService departService;
	@Autowired
	private HttpSession session;
	/**
	 * 进入DApprove界面
	 * @return
	 */
	@RequestMapping(value="/initScientificDepart", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView initScientificDepart() {
		ModelAndView mod= new ModelAndView("/review/scientificDepart.btl");
		return mod;
	}
	//进入EApprove界面
	@RequestMapping(value="/initScientificCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initScientificCollege','000100')")
    public ModelAndView initScientificCollege(){
		//列表
		List<SysDepart> departs=departService.getAllDepartOne();
		ModelAndView mod= new ModelAndView("/review/scientificCollege.btl");
		mod.addObject("departs", departs);
		return mod;
	}
	/**
	 * 查询院已通过的科研课题
	 * @return
	 */
	@RequestMapping(value="/queryScientificCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryScientificCourtyard','000200000')")
	public Map<String,Object> queryScientificCollege(RptScientific scientific,Integer pageNo,Integer maxResults) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				scientific.setStatus("DApprove");
				scientific.setBelongCycle(cycle.getCycleName());
				List<RptScientific> rows=scientificService.getScientificContent(scientific, pageNo, maxResults);
				Integer total=scientificService.getScientificCount(scientific);
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
	 * 查询本系所有人CApprove的
	 * @param scie
	 * @param pageNo
	 * @param maxResults
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/queryScientificDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryScientificDepart','000100000')")
	@PowerJsonFilters({@PowerJsonFilter(clazz = ScientificApproveAction.class, include =  { "scieId" ,"scieName","scieType","scieDepart","scieLeader","scieStartDate","scieCloseDate"})})
	public Map<String,Object> queryScientificDepart(RptScientific scie,Integer pageNo,Integer maxResults,HttpSession session){
		LOG.debug("scieId:"+scie.getScieId());
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				String parentDepart=user.getStaffParentDepart();
				if (null==parentDepart||parentDepart.equals("")) {
					scie.setStaffDepart(user.getStaffDepart());
				}else {
					scie.setStaffParentDepart(parentDepart);
				}
				
				scie.setStatus("CApprove");
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				scie.setBelongCycle(cycle.getCycleName());
				List<RptScientific> rows=scientificService.getScientificContent(scie, pageNo, maxResults);
				Integer total=scientificService.getScientificCount(scie);
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
	 * DApprove通过
	 * @param screType
	 * @param scieId
	 * @param oldStatus
	 * @param newStatus
	 * @param reason
	 * @return
	 */
	@RequestMapping(value="/passScientificDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/passScientificDepart','000100001')")
	public String passScientificDepart(Integer scieId,String memo) {
		String mess="";
		try {
			scientificService.approveScientificStatus(scieId,"通过","CApprove","DApprove",memo);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
	/**
	 * 系拒绝
	 * @param screType
	 * @param scieId
	 * @param newStatus
	 * @param memo
	 * @return
	 */
	@RequestMapping(value="/noPassScientificDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/noPassScientificDepart','000100002')")
	public String noPassScientificDepart(Integer scieId,String memo) {
		String mess="";
		try {
			scientificService.approveScientificStatus(scieId,"拒绝","CApprove","BApprove",memo);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
	
	/**
	 * EApprove通过
	 * @param screType
	 * @param scieId
	 * @param oldStatus
	 * @param newStatus
	 * @param reason
	 * @return
	 */
	@RequestMapping(value="/passScientificCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/passScientificCollege','000200001')")
	public String passScientificCollege(Integer scieId,String memo) {
		String mess="";
		try {
			scientificService.approveScientificStatus(scieId,"通过","DApprove","EApprove",memo);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
	/**
	 * 院拒绝
	 * @param screType
	 * @param scieId
	 * @param newStatus
	 * @param memo
	 * @return
	 */
	@RequestMapping(value="/noPassScientificCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/noPassScientificCollege','000200002')")
	public String noPassScientificCollege(Integer scieId,String memo) {
		String mess="";
		try {
			scientificService.approveScientificStatus(scieId,"拒绝","DApprove","BApprove",memo);
			mess="succ";
		} catch (Exception e) {
			e.printStackTrace();
			mess=e.toString();
		}
		return mess;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	
}
