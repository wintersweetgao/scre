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

import edu.hfu.scre.entity.RptAcademic;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;
@RestController
@RequestMapping(value="/review")
public class AcademicApproveAction {
		private static final Logger LOG = LoggerFactory.getLogger(AcademicApproveAction.class);
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
		@RequestMapping(value="/initAcademicDepart", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/initAcademicDepart','000104')")
		public ModelAndView initAcademicDepart() {
			ModelAndView mod= new ModelAndView("/review/academicDepart.btl");
			return mod;
		}
		//进入EApprove界面
		@RequestMapping(value="/initAcademicCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/initAcademicCollege','000204')")
	    public ModelAndView initAcademicCollege(){
			//列表
			List<SysDepart> departs=departService.getAllDepartOne();
			ModelAndView mod= new ModelAndView("/review/academicCollege.btl");
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
		@RequestMapping(value="/queryAcademicDepart", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/queryAcademicDepart','000104000')")
		public Map<String,Object> queryAcademicDepart(RptAcademic academic,Integer pageNo,Integer maxResults){
			Map<String,Object> rtnMap=new HashMap<>();
			try {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				Object principal=auth.getPrincipal();
				if (principal != null && principal instanceof MyUserDetails) {
					MyUserDetails usd = (MyUserDetails) principal;
					SysStaff user=(SysStaff)usd.getCustomObj();
					String parentDepart=user.getStaffParentDepart();
					if (null==parentDepart||parentDepart.equals("")) {
						academic.setStaffDepart(user.getStaffDepart());
					}else {
						academic.setStaffParentDepart(parentDepart);
					}
					academic.setStatus("CApprove");
				}
				SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
				if (null==cycle) {
					rtnMap.put("mess", "未获取到科研周期！");
					rtnMap.put("total", 0);
					rtnMap.put("rows", null);
				}else {
					academic.setBelongCycle(cycle.getCycleName());
					List<RptAcademic> rows=commonService.getContentByCon(academic, pageNo, maxResults);
					Integer total=commonService.getCountByCon(academic);
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
		@RequestMapping(value="/queryAcademicCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/queryAcademicCollege','000204000')")
		public Map<String,Object> queryAcademicCollege(RptAcademic academic,Integer pageNo,Integer maxResults){
			Map<String,Object> rtnMap=new HashMap<>();
			try {
				SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
				if (null==cycle) {
					rtnMap.put("mess", "未获取到科研周期！");
					rtnMap.put("total", 0);
					rtnMap.put("rows", null);
				}else {
					academic.setStatus("DApprove");
					academic.setBelongCycle(cycle.getCycleName());
					List<RptAcademic> rows=commonService.getContentByCon(academic, pageNo, maxResults);
					Integer total=commonService.getCountByCon(academic);
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
		@RequestMapping(value="/passAcademicDepart", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/passAcademicDepart','000104001')")
		public String passAcademicDepart(Integer academicId,String memo) {
			String mess="";
			try {
				commonService.passScreStatus(new RptAcademic(), "academicId", academicId, "CApprove", "DApprove", memo);
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
		@RequestMapping(value="/refuseAcademicDepart", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/refuseAcademicDepart','000104002')")
		public String refuseAcademicDepart(Integer academicId,String memo) {
			String mess="";
			try {
				commonService.refuseScreStatus(new RptAcademic(), "academicId", academicId, "CApprove", "BApprove", memo);
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
		@RequestMapping(value="/passAcademicCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/passAcademicCollege','000204001')")
		public String passAcademicCollege(Integer academicId,String memo) {
			String mess="";
			try {
				commonService.passScreStatus(new RptAcademic(), "academicId", academicId, "DApprove", "EApprove", memo);
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
		@RequestMapping(value="/refuseAcademicCollege", method= {RequestMethod.GET,RequestMethod.POST})
		@PreAuthorize("hasPermission('/refuseAcademicCollege','000204002')")
		public String refuseAcademicCollege(Integer academicId,String memo) {
			String mess="";
			try {
				commonService.refuseScreStatus(new RptAcademic(), "academicId", academicId, "DApprove", "BApprove", memo);
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

