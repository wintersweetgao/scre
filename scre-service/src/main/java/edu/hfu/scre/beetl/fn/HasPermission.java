package edu.hfu.scre.beetl.fn;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class HasPermission implements Function{

	/*
	 * call方法有俩个参数，第一个是数组，这是由模板传入的，对应着模板的参数，
	 * 	第二个是Context，包含了模板的上下文，主要提供了如下属性	 * 
	 *  byteWriter 输出流 
	 *  template 模板本身
	 *  gt GroupTemplate 
	 *  globalVar 该模板对应的全局变量
	 *  byteOutputMode 模板的输出模式，是字节还是字符
	 *  safeOutput 模板当前是否处于安全输出模式 
	   *     其他属性建议不熟悉的开发人员不要乱动
	 */
	@Override
	public Boolean call(Object[] paras, Context ctx) {
		if(null!=paras&&paras.length>0) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String grant="ROLE_"+String.valueOf(paras[0]);
			for(GrantedAuthority ga : auth.getAuthorities()) {//authentication 为在注释1 中循环添加到 GrantedAuthority 对象中的权限信息集合
	            if (grant.equals(ga.getAuthority())) {
	            	return true;
	            }
	         }
			return false;
		}else {
			return false;
		}
	}
	
	

}
