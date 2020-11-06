package edu.hfu.scre.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class Permission {
	
	/*
	 * 判断权限集合里是否包含指定权限代码
	 */
	public static boolean isHasPermisson(String grantCode) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		for(GrantedAuthority ga : auth.getAuthorities()) {//
			if (ga.getAuthority().equals("ROLE_"+grantCode)) {
				return true;
			}
		}
		return false;
	}
}
