package cn.devezhao.commons;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

/**
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 * @since 0.2, 2014-4-18
 */
public class RegexUtils {

	public static final Pattern EMAIL_PATTERN = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	public static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
	public static final Pattern CNMOBILE_PATTERN = Pattern.compile("^(1[3-9][0-9])\\d{8}$");
	public static final Pattern TEL_PATTERN = Pattern.compile("[0-9\\-]{7,18}");
	
	/**
	 * @param email
	 * @return
	 */
	public static boolean isEMail(String email) {
		return StringUtils.isNotBlank(email) && EMAIL_PATTERN.matcher(email).find()
				&& !(email.endsWith("yahoo.cn") || email.endsWith("yahoo.com.cn") || email.endsWith(".")
				|| email.startsWith(".") || StringUtils.countMatches(email, "@") > 1 || email.contains(".."));
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
	public static boolean isCNMobile(String mobile) {
		if (StringUtils.isBlank(mobile) || mobile.length() < 11) {
			return false;
		}
		if (mobile.startsWith("+86")) {
			mobile = mobile.substring(3);
		} else if (mobile.startsWith("86")) {
			mobile = mobile.substring(2);
		}
		return CNMOBILE_PATTERN.matcher(mobile).find();
	}
	
	/**
	 * @param tel
	 * @return
	 */
	public static boolean isTel(String tel) {
		if (StringUtils.isBlank(tel) || tel.length() < 7) {
			return false;
		}
		if (tel.startsWith("-") || tel.endsWith("-") || tel.contains("--")) {
			return false;
		}
		return TEL_PATTERN.matcher(tel).find();
	}
}
