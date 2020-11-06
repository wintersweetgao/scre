package edu.hfu.scre.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.hfu.scre.entity.SysHfirstPicture;
import edu.hfu.scre.entity.SysNotice;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.service.security.MyUserDetails;
import edu.hfu.scre.service.security.UserGrantService;
import edu.hfu.scre.service.sysset.HfirstPictureService;
import edu.hfu.scre.service.sysset.NoticeService;

@Controller
@RequestMapping("/")
public class LoginIndexAction {
	private static final Logger LOG = LoggerFactory.getLogger(LoginIndexAction.class);
	/** 本系统的授权代码 */
	@Value("${auth.grantCode}")
	String grantCode;
	/** auth系统地址 */
	@Value("${auth.server.url}")
	String authUrl;
	@Resource
	UserGrantService userGrantService;
	@Resource
	NoticeService noticeservice;
	@Resource
	HfirstPictureService hfirstPictureService;
	@RequestMapping(value = { "/", "/login" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView gotoLoginIndex(String mess) {
		LOG.debug("进入登录页...");
		ModelAndView mod = new ModelAndView("/hlcIndex.btl");
		try {
			List<SysHfirstPicture> lsp = hfirstPictureService.findfirstPictureById();
			mod.addObject("lsp", lsp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (null == mess) {
			mess = "";
		}
		mod.addObject("mess", mess);
		// 添加auth注册时返回的地址
		mod.addObject("authUrlReg", authUrl + "/userregistration");
		return mod;
		
	}

	@RequestMapping(value = { "/timeout" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String timeout() {
		return "/error.btl";
	}

	@RequestMapping(value = { "/goBlank" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String goBlank() {
		return "/blank.btl";
	}

	@RequestMapping(value = { "/invalidSession" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String invalidSession() {
		return "/invalidSession.btl";
	}

	@RequestMapping(value = { "/goIndex" }, method = { RequestMethod.POST, RequestMethod.GET })
	@PreAuthorize("hasRole('ROLE_00')")
	public String goIndex() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal != null && principal instanceof MyUserDetails) {
			MyUserDetails usd = (MyUserDetails) principal;
			SysStaff user = (SysStaff) usd.getCustomObj();
			LOG.info("当前登陆用户：" + user.getStaffName() + "," + user.getStaffDepart() + "," + user.getStaffTitle() + ","
					+ user.getPoststr() + "," + user.getStaffParentDepart());
		}
		return "/index.btl";
	}

	@RequestMapping(value = { "/loginOut" }, method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String loginOut(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "success";
	}

	// @RequestMapping(value="/error", method=RequestMethod.GET)
	public void loginError(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		AuthenticationException exception = (AuthenticationException) request.getSession()
				.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		if (null != exception) {
			LOG.debug("发生错误..." + exception.toString());
			try {
				exception.printStackTrace();
				response.getWriter().write(exception.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			LOG.debug("发生权限验证错误...exception is null");
		}
	}

}
