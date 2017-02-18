/*
 Copyright (C) 2011-2014 QIDAPP.com. All rights reserved.
 QIDAPP.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package cn.devezhao.commons;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Zhao Fangfang
 * @version $Id: RegexUtils.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 * @since 0.2, 2014-4-18
 */
public class RegexUtils {

	public static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	public static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
	public static final Pattern MOBILE_PATTERN = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
	
	/**
	 * @param email
	 * @return
	 */
	public static boolean isEMail(String email) {
		return StringUtils.isNotBlank(email) && EMAIL_PATTERN.matcher(email).find();
	}
	
	/**
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		return StringUtils.isNotBlank(url) && URL_PATTERN.matcher(url).find();
	}
	
	/**
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		return StringUtils.isNotBlank(mobile) && MOBILE_PATTERN.matcher(mobile).find();
	}
}
