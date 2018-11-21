package com.wis.basis.common.utils.security;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class MD5Utils {
	public static String passwordEncoder(String password, Object salt) {
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		return md5.encodePassword(password, salt);
	}
	
	public static void main(String[] args){
		
		System.out.println(passwordEncoder("1", "hr"));
	}
}
