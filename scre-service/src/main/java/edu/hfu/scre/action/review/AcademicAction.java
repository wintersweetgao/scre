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
import org.hibernate.exception.ConstraintViolationException;

import edu.hfu.scre.entity.RptAcademic;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.DictionaryService;

@RestController
@RequestMapping(value="/review")
public class AcademicAction {
	private static final Logger LOG = LoggerFactory.getLogger(AcademicAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	@Resource
	private DictionaryService dictionaryService;
	
	/**
	 * 学术
	 * @return
	 */
	@RequestMapping(value="/initAcademic", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initAcademic','000004')")
	public ModelAndView initAcademic(){
		ModelAndView mod= new ModelAndView("/review/academic.btl");
		List<SysDictionary> ls=dictionaryService.getDictonaryByType("学术报告");
		mod.addObject("dicts", ls);
		return mod;
	} 
	
	
	/**
	 * 查询学术
	 * @return
	 */
	@RequestMapping(value="/queryAcademic", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryAcademic','000004000')")
	public Map<String,Object> queryAcademic(RptAcademic academic,Integer pageNo,Integer maxResults) {
		LOG.debug("academicTopic:"+academic.getAcademicTopic());
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				academic.setStaffName(user.getStaffName());
				academic.setStaffTitle(user.getStaffTitle());
				academic.setStaffParentDepart(user.getStaffParentDepart());
				academic.setStaffDepart(user.getStaffDepart());
				academic.setUserCode(user.getUserCode());
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
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}
	@RequestMapping(value="/saveAcademic", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/saveAcademic','000004001')")
	public String  saveAcademic(RptAcademic academic) {
		LOG.debug("getAcademicTopic:"+academic.getAcademicTopic());
		LOG.debug("getAcademicDate:"+academic.getAcademicDate());
		String mess="";
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				mess= "未获取到科研周期！";
			}else {
				academic.setBelongCycle(cycle.getCycleName());
				mess= commonService.saveScre(academic);
			}
		}  catch (ConstraintViolationException con){
		      con.printStackTrace();
		      mess="重复添加";
		    }catch (Exception e) {
		      e.printStackTrace();
		      mess =e.toString().replace("java.lang.RuntimeException:","");
		    }
		return mess;
	}
	
	@RequestMapping(value="/updAcademic", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/updAcademic','000004002')")
	public String updAcademic(RptAcademic academic) {
		LOG.debug("academicId:"+academic.getAcademicId());
		String mess="";
		try {
			mess=commonService.updScre(academic, academic.getAcademicId());
		} catch (Exception e) {
			mess=e.toString();
			if (mess.indexOf("constraint")>-1) {
				mess="报告主题重复，请修正！";
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
	@RequestMapping(value="/delAcademic", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delAcademic','000004006')")
	public Map<String,Object> delAcademic(Integer academicId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			RptAcademic academic=new RptAcademic();
			academic.setAcademicId(academicId);
			commonService.delScre(academic,"academicId", memo);
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
	 * @param academicId
	 * @param memo
	 * @return
	 */
	@RequestMapping(value="/submitacademic", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/submitacademic','000004003')")
	public Map<String, Object> submitacademic(Integer academicId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			commonService.passScreStatus(new RptAcademic(), "academicId", academicId, "BApprove", "CApprove", memo);
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
