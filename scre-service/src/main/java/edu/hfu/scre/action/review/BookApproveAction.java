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

import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.SysDepart;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.DepartService;
import edu.hfu.scre.service.security.MyUserDetails;

@RestController
@RequestMapping(value="/review")
public class BookApproveAction {
	private static final Logger LOG = LoggerFactory.getLogger(BookApproveAction.class);
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
	@RequestMapping(value="/initBookDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initBookDepart','000101')")
	public ModelAndView initBookDepart() {
		ModelAndView mod= new ModelAndView("/review/bookDepart.btl");
		return mod;
	}
	//进入EApprove界面
	@RequestMapping(value="/initBookCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initBookCollege','000201')")
    public ModelAndView initBookCollege(){
		//列表
		List<SysDepart> departs=departService.getAllDepartOne();
		ModelAndView mod= new ModelAndView("/review/bookCollege.btl");
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
	@RequestMapping(value="/queryBookDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryBookDepart','000101000')")
	public Map<String,Object> queryBookDepart(RptBook book,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				String parentDepart=user.getStaffParentDepart();
				if (null==parentDepart||parentDepart.equals("")) {
					book.setStaffDepart(user.getStaffDepart());
				}else {
					book.setStaffParentDepart(parentDepart);
				}
				book.setStatus("CApprove");
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				book.setBelongCycle(cycle.getCycleName());
				List<RptBook> rows=commonService.getContentByCon(book, pageNo, maxResults);
				Integer total=commonService.getCountByCon(book);
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
	@RequestMapping(value="/queryBookCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryBookCollege','000201000')")
	public Map<String,Object> queryBookCollege(RptBook Book,Integer pageNo,Integer maxResults){
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
				Book.setStatus("DApprove");
				Book.setBelongCycle(cycle.getCycleName());
				List<RptBook> rows=commonService.getContentByCon(Book, pageNo, maxResults);
				Integer total=commonService.getCountByCon(Book);
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
	@RequestMapping(value="/passBookDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/passBookDepart','000101001')")
	public String passBookDepart(Integer bookID,String memo) {
		String mess="";
		try {
			commonService.passScreStatus(new RptBook(),"BookID",bookID, "CApprove", "DApprove", memo);
			
			mess="succ";
		} catch (Exception e) {
			LOG.error(e.getMessage());
			LOG.debug("id"+bookID+" memo"+memo);
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
	@RequestMapping(value="/refuseBookDepart", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/refuseBookDepart','000101002')")
	public String refuseBookDepart(Integer bookID,String memo) {
		String mess="";
		try {
			commonService.refuseScreStatus(new RptBook(), "BookID", bookID, "CApprove", "BApprove", memo);
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
	@RequestMapping(value="/passBookCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/passBookCollege','000201001')")
	public String passBookCollege(Integer bookID,String memo) {
		String mess="";
		try {
			commonService.passScreStatus(new RptBook(), "BookID", bookID, "DApprove", "EApprove", memo);
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
	@RequestMapping(value="/refuseBookCollege", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/refuseBookCollege','000201002')")
	public String refuseBookCollege(Integer bookID,String memo) {
		String mess="";
		try {
			commonService.refuseScreStatus(new RptBook(), "BookID", bookID, "DApprove", "BApprove", memo);
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
