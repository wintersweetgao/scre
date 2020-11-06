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
import edu.hfu.scre.entity.RptPatent;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;

@RestController
@RequestMapping(value="/review")
public class PatentApproveAction {
	private static final Logger LOG = LoggerFactory.getLogger(PatentApproveAction.class);
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
	@RequestMapping(value="/initPatentDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initPatentDepart','000103')")
	public ModelAndView initPatentrDepart() {
		ModelAndView mod= new ModelAndView("/review/patentDepart.btl");
		return mod;
	}
	//进入EApprove界面
		@RequestMapping(value="/initPatentCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/initPatentCollege','000203')")
	    public ModelAndView initPatentCollege(){
			//列表
			List<SysDepart> departs=departService.getAllDepartOne();
			ModelAndView mod= new ModelAndView("/review/patentCollege.btl");
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
		@RequestMapping(value="/queryPatentDepart", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/queryPatentDepart','000103000')")
		public Map<String,Object> queryPatentDepart(RptPatent patent,Integer pageNo,Integer maxResults){
			Map<String,Object> rtnMap=new HashMap<>();
			try {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				Object principal=auth.getPrincipal();
				if (principal != null && principal instanceof MyUserDetails) {
					MyUserDetails usd = (MyUserDetails) principal;
					SysStaff user=(SysStaff)usd.getCustomObj();
					String parentDepart=user.getStaffParentDepart();
					if (null==parentDepart||parentDepart.equals("")) {
						patent.setStaffDepart(user.getStaffDepart());
					}else {
						patent.setStaffParentDepart(parentDepart);
					}
					patent.setStatus("CApprove");
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
		@RequestMapping(value="/queryPatentCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/queryPatentCollege','000203000')")
		public Map<String,Object> queryPatentCollege(RptPatent patent,Integer pageNo,Integer maxResults){
			Map<String,Object> rtnMap=new HashMap<>();
			try {
				SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
				if (null==cycle) {
					rtnMap.put("mess", "未获取到科研周期！");
					rtnMap.put("total", 0);
					rtnMap.put("rows", null);
				}else {
					patent.setStatus("DApprove");
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
		@RequestMapping(value="/passPatentDepart", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/passPatentDepart','000103001')")
		public String passPatentDepart(Integer patentId,String memo) {
			String mess="";
			try {
				commonService.passScreStatus(new RptPatent(), "patentId", patentId, "CApprove", "DApprove", memo);
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
		@RequestMapping(value="/refusePatentDepart", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/refusePatentDepart','000103002')")
		public String refusePatentDepart(Integer patentId,String memo) {
			String mess="";
			try {
				commonService.refuseScreStatus(new RptPatent(), "patentId", patentId, "CApprove", "BApprove", memo);
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
		@RequestMapping(value="/passPatentCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/passPatentCollege','000203001')")
		public String passPatentCollege(Integer patentId,String memo) {
			String mess="";
			try {
				commonService.passScreStatus(new RptPatent(), "patentId", patentId, "DApprove", "EApprove", memo);
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
		@RequestMapping(value="/refusePatentCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/refusePatentCollege','000203002')")
		public String refusePaperCollege(Integer patentId,String memo) {
			String mess="";
			try {
				commonService.refuseScreStatus(new RptPatent(), "patentId", patentId, "DApprove", "BApprove", memo);
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
