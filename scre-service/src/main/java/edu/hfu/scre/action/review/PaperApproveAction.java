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


import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;

@RestController
@RequestMapping(value="/review")
public class PaperApproveAction {
	private static final Logger LOG = LoggerFactory.getLogger(PaperApproveAction.class);
	@Resource
	DepartService departService;
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	/**
	 * 进入DApprove界面
	 * @return
	 */
	@RequestMapping(value="/initPaperDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initPaperDepart','000102')")
	public ModelAndView initPaperDepart() {
		ModelAndView mod= new ModelAndView("/review/paperDepart.btl");
		return mod;
	}
	//进入EApprove界面
	@RequestMapping(value="/initPaperCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initPaperCollege','000202')")
    public ModelAndView initPaperCollege(){
		//列表
		List<SysDepart> departs=departService.getAllDepartOne();
		ModelAndView mod= new ModelAndView("/review/paperCollege.btl");
		mod.addObject("departs", departs);
		return mod;
	}
	
	/**
	 * 查询本系所有人CApprove的
	 * @param scie
	 * @param pageNo
	 * @param maxResults
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/queryPaperDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryPaperDepart','000102000')")
	public Map<String,Object> queryPaperDepart(RptPaper paper,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				String parentDepart=user.getStaffParentDepart();
				if (null==parentDepart||parentDepart.equals("")) {
					paper.setStaffDepart(user.getStaffDepart());
				}else {
					paper.setStaffParentDepart(parentDepart);
				}
				paper.setStatus("CApprove");
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				paper.setBelongCycle(cycle.getCycleName());
				List<RptPaper> rows=commonService.getContentByCon(paper, pageNo, maxResults);
				Integer total=commonService.getCountByCon(paper);
				String mess="succ";
				rtnMap.put("total", total);
				rtnMap.put("rows", rows);
				rtnMap.put("mess", mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}
	/**
	 * 查询本院所有人CApprove的
	 * @param scie
	 * @param pageNo
	 * @param maxResults
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/queryPaperCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryPaperCollege','000202000')")
	public Map<String,Object> queryPaperCollege(RptPaper paper,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				paper.setStatus("DApprove");
				paper.setBelongCycle(cycle.getCycleName());
				List<RptPaper> rows=commonService.getContentByCon(paper, pageNo, maxResults);
				Integer total=commonService.getCountByCon(paper);
				String mess="succ";
				rtnMap.put("total", total);
				rtnMap.put("rows", rows);
				rtnMap.put("mess", mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
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
	@RequestMapping(value="/passPaperDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/passPaperDepart','000102001')")
	public String passPaperDepart(Integer paperId,String memo) {
		String mess="";
		try {
			commonService.passScreStatus(new RptPaper(), "paperId", paperId, "CApprove", "DApprove", memo);
			mess="succ";
		} catch (Exception e) {
			LOG.error(e.getMessage());
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
	@RequestMapping(value="/refusePaperDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/refusePaperDepart','000102002')")
	public String refusePaperDepart(Integer paperId,String memo) {
		String mess="";
		try {
			commonService.refuseScreStatus(new RptPaper(), "paperId", paperId, "CApprove", "BApprove", memo);
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
	@RequestMapping(value="/passPaperCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/passPaperCollege','000202001')")
	public String passPaperCollege(Integer paperId,String memo) {
		String mess="";
		try {
			commonService.passScreStatus(new RptPaper(), "paperId", paperId, "DApprove", "EApprove", memo);
			mess="succ";
		} catch (Exception e) {
			LOG.error(e.getMessage());
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
	@RequestMapping(value="/refusePaperCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/refusePaperCollege','000202002')")
	public String refusePaperCollege(Integer paperId,String memo) {
		String mess="";
		try {
			commonService.refuseScreStatus(new RptPaper(), "paperId", paperId, "DApprove", "BApprove", memo);
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
