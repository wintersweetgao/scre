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
import edu.hfu.scre.entity.RptBook;
import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.entity.xmlBean.SysDictionary;
import edu.hfu.scre.service.review.CommonScreService;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.sysset.DictionaryService;

@RestController
@RequestMapping(value="/review")
public class BookAction {
	private static final Logger LOG = LoggerFactory.getLogger(BookAction.class);
	@Autowired
	private HttpSession session;
	@Resource
	CommonScreService commonService;
	@Resource
	private DictionaryService dictionaryService;
	/**
	 * 教材，著作成果
	 *@author tanzhi
	 */
	@RequestMapping(value="/initBook", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/initBook','000001')")
	public ModelAndView initBook(){
		ModelAndView mod= new ModelAndView("/review/book.btl");
		List<SysDictionary> ls=dictionaryService.getDictonaryByType("科研论文");
		mod.addObject("dicts", ls);
		return mod;
	}
	/**
	 * 查询科研课题提交内容
	 * @return
	 */
	@RequestMapping(value="/queryBook", method= {RequestMethod.GET,RequestMethod.POST})
	public Map<String,Object> queryBook(RptBook Book,Integer pageNo,Integer maxResults) {
		LOG.debug("BookName:"+Book.getBookName());
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal=auth.getPrincipal();
			if (principal != null && principal instanceof MyUserDetails) {
				MyUserDetails usd = (MyUserDetails) principal;
				SysStaff user=(SysStaff)usd.getCustomObj();
				Book.setStaffName(user.getStaffName());
				Book.setStaffTitle(user.getStaffTitle());
				Book.setStaffParentDepart(user.getStaffParentDepart());
				Book.setStaffDepart(user.getStaffDepart());
				Book.setUserCode(user.getUserCode());
			}
			SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
			if (null==cycle) {
				rtnMap.put("mess", "未获取到科研周期！");
				rtnMap.put("total", 0);
				rtnMap.put("rows", null);
			}else {
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
			rtnMap.put("total", 0);
			rtnMap.put("rows", null);
			rtnMap.put("mess", e.toString());
		}
		return rtnMap;
	}

	@RequestMapping(value="/saveBook", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/saveBook','000001001')")
	public String  saveBook(RptBook book) {
	    LOG.debug("getBookName:"+book.getBookName());
	    LOG.debug("getFirstEditor:"+book.getFirstEditor());
	    String mess="";
	    try {
	      SysScreCycle cycle=(SysScreCycle) session.getAttribute("cycle");
	      if (null==cycle) {
	        mess= "未获取到科研周期！";
	      }else {
	        book.setBelongCycle(cycle.getCycleName());
	        mess= commonService.saveScre(book);
	      }
	    } catch (ConstraintViolationException con){
	    	con.printStackTrace();
	    	mess="教材、著作名称相同，请重新命名！";
	    }catch (Exception e) {
	    	e.printStackTrace();
	    	mess =e.toString().replace("java.lang.RuntimeException:", "");
	    }
	    return mess;
	  }
	@RequestMapping(value="/updBook", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/updBook','000001003')")
	public String updBook(RptBook Book) {
		LOG.debug("BookID:"+Book.getBookID());
		String mess="";
		try {
			mess=commonService.updScre(Book, Book.getBookID());
		} catch(ConstraintViolationException con) {
			con.printStackTrace();
			mess="修改教材、著作名称时相同，请重新命名！";
		} catch (Exception e) {
			mess=e.toString();
			mess =e.toString().replace("java.lang.RuntimeException:", "");
		}
		return mess;
	}
	
	/**
	 * 删除
	 * @param BookID
	 * @param memo
	 * @return
	 */
	@RequestMapping(value="/delBook", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/delBook','000001006')")
	public Map<String,Object> delBook(Integer bookID,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			RptBook book=new RptBook();
			book.setBookID(bookID);
			commonService.delScre(book,"bookID", memo);
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
	 * @param BookId
	 * @param memo
	 * @return
	 */
	@RequestMapping(value="/submitBook", method= {RequestMethod.GET,RequestMethod.POST})
	@PreAuthorize("hasPermission('/submitBook','000001004')")
	public Map<String, Object> submitBook(Integer bookID,String memo) {
		Map<String,Object> rtnMap=new HashMap<>();
		try {
			commonService.passScreStatus(new RptBook(), "BookID", bookID, "BApprove", "CApprove", memo);
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
