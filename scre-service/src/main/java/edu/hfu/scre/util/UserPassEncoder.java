package edu.hfu.scre.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPassEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return MD5Util.string2MD5(rawPassword.toString());
	}

	/**
	 * rawPassword 用户输出的密码
	 * encodedPassword  数据库中的密码
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(MD5Util.string2MD5(rawPassword.toString()));
	}

}
