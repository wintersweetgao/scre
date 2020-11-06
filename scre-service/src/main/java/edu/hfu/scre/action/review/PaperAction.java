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

import edu.hfu.scre.entity.RptPaper;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.DictionaryService;

@RestController
@RequestMapping(value="/review")
public class PaperAction {
	private static final Logger LOG = LoggerFactory.getLogger(PaperAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	@Resource
	private DictionaryService dictionaryService;
	/**
	 * 个人提交论文
	 * @return
	 */
	@RequestMapping(value="/initPaper", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initPaper','000002')")
	public ModelAndView initPaper(){
		ModelAndView mod= new ModelAndView("/review/paper.btl");
		List<SysDictionary> ls=dictionaryService.getDictonaryByType("科研论文");
		mod.addObject("dicts", ls);
		return mod;
	} 
	
	
	/**
	 * 查询科研课题提交内容
	 * @return
	 */
	@RequestMapping(value="/queryPaper", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/queryPaper','000002000')")
	public Map<String,Object> queryPaper(RptPaper paper,Integer pageNo,Integer maxResults) {
		LOG.debug("paperThesis:"+paper.getPaperThesis());
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				paper.setStaffName(user.getStaffName());
				paper.setStaffTitle(user.getStaffTitle());
				paper.setStaffParentDepart(user.getStaffParentDepart());
				paper.setStaffDepart(user.getStaffDepart());
				paper.setUserCode(user.getUserCode());
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
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}

	@RequestMapping(value="/savePaper", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/savePaper','000002001')")
	public String  savePaper(RptPaper paper) throws Exception {
		LOG.debug("getPaperSponsor:"+paper.getPaperSponsor());
		LOG.debug("getPaperThesis:"+paper.getPaperThesis());
		String mess="";
		try {
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				mess= "未获取到科研周期！";
			}else {
				paper.setBelongCycle(cycle.getCycleName());
				mess= commonService.saveScre(paper);
			}
		} catch (ConstraintViolationException e) {
			mess = e.toString();
			e.printStackTrace();
			mess = "增加论文题目重复";
		} catch (Exception e) {
			e.printStackTrace();
			mess = e.toString().replace("java.lang.RuntimeException:", "");
		}

		return mess;
	}
	@RequestMapping(value="/updPaper", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/updPaper','000002002')")
	public String updPaper(RptPaper paper) {
		LOG.debug("paperId:"+paper.getPaperId());
		String mess="";
		try {
			mess=commonService.updScre(paper, paper.getPaperId());
		} catch (Exception e) {
			mess=e.toString();
			if (mess.indexOf("constraint")>-1) {
				mess="修改论文题目重复";
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
	@RequestMapping(value="/delPaper", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delPaper','000002006')")
	public Map<String,Object> delPaper(Integer paperId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			RptPaper paper=new RptPaper();
			paper.setPaperId(paperId);
			commonService.delScre(paper,"paperId", memo);
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
	@RequestMapping(value="/submitPaper", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/submitPaper','000002003')")
	public Map<String, Object> submitPaper(Integer paperId,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			commonService.passScreStatus(new RptPaper(), "paperId", paperId, "BApprove", "CApprove", memo);
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
