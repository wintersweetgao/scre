package edu.hfu.scre.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import edu.hfu.scre.entity.SysScreCycle;
import edu.hfu.scre.service.security.UserGrantService;
import edu.hfu.scre.service.sysset.ScreCycleService;
import edu.hfu.scre.util.HttpTool;
import edu.hfu.scre.util.UserPassEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
	@Autowired
	UserGrantService userService;
	@Autowired
	ScreCycleService screCycleService;
	
	@Bean
	public PasswordEncoder myPasswordEncoder() {
		 return new UserPassEncoder(); 
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(myPasswordEncoder());
	}


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/login", "/static/**","/","/timeout",
				"/goBlank","/error","/loginOut","/invalidSession","/getNoticee","/queryViewExhibition");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().and().formLogin().loginPage("/").loginProcessingUrl("/userLogin")
		.usernameParameter("userCode").passwordParameter("userPass").permitAll()
		.failureHandler(new AuthenticationFailureHandler() {
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
				String msg=e.toString().replace("org.springframework.security.authentication.InternalAuthenticationServiceException:", "");
				if(msg.indexOf("Bad credentials")>-1) {
					msg="账号或密码错误";
				}
				String path="/?mess="+msg;
				request.getRequestDispatcher(path).forward(request,response);
			}
		}).successHandler(new AuthenticationSuccessHandler() {
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				try {
					//登录成功，将必要信息保存到session
					HttpSession session=request.getSession();
					//写入当前 评估周期
					SysScreCycle cycle=screCycleService.getLastCycle();
					if (null!=cycle) {
						session.setAttribute("cycle", cycle);
					}
					request.getRequestDispatcher("/goIndex").forward(request,response);
				} catch (Exception e) {
					e.printStackTrace();
					String path="/?mess="+e.toString().replace("org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.springframework.security.access.AccessDeniedException: ", "");
					request.getRequestDispatcher(path).forward(request,response);
				}
				
			}
		}).and().logout().permitAll()
		//and().headers().frameOptions().sameOrigin() 可以解决页面不允许在iframe打开的问题
		.and().headers().frameOptions().sameOrigin()
		.and().csrf().disable().exceptionHandling().accessDeniedHandler(authenticationAccessDeniedHandler)
		.and().sessionManagement().invalidSessionUrl("/invalidSession").maximumSessions(1)
		// 当达到最大值时，是否保留已经登录的用户 为true，新用户无法登录；为 false，旧用户被踢出
		.maxSessionsPreventsLogin(false)
		.expiredSessionStrategy(new SessionInformationExpiredStrategy() {
			@Override
			public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
				HttpServletResponse response= event.getResponse();
				Map<String,String> mess=new HashMap<>();
				mess.put("mess",  "已经另一台机器登录，您被迫下线。" + event.getSessionInformation().getLastRequest());
				HttpTool.writeToClient(response, mess);
			}
		});
		
	}
	
}
