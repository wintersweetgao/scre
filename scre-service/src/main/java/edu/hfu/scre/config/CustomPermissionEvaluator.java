package edu.hfu.scre.config;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator{
	private static final Logger LOG = LoggerFactory.getLogger(CustomPermissionEvaluator.class);
	/**
	 * authentication userDetail 中循环添加到 GrantedAuthority 对象中的权限信息集合
	 * targetDomainObject : 注解传过来的要访问的路径
	 * permission：注解传过来的 访问该路径需要的权限
	 */
	@Override
	public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
		LOG.debug("targetDomainObject:"+targetDomainObject.toString());
		LOG.debug("permission:"+permission.toString());
		/**
		  * 正确做法是获取当前用户的权限，以及权限对应的可访问的路径与传入的targetDomainObject 进行对比
		 * SysStaff user=(SysStaff) auth.getAuthorities();
		 */
		String[] permissions=permission.toString().split(",");
		for(GrantedAuthority ga : auth.getAuthorities()) {//
			for (String per:permissions) {
				if (("ROLE_"+per).equals(ga.getAuthority())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
