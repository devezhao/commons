package cn.devezhao.commons.web;

import javax.servlet.ServletRequest;

/**
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class WebUtils {
	
	public static final String ENCODING_UTF8 	= "UTF-8";
	public static final String ENCODING_GBK 	= "GBK";
	public static final String ENCODING_DEFAULT = ENCODING_UTF8;

	// ---------------------------------------------- TOKENs

	/**
	 * Key前缀  */
	public static final String KEY_PREFIX 		= WebUtils.class.getPackage().getName() + "__";
	/**
	 * 当前会话中的用户 */
	public static final String CURRENT_USER 	= KEY_PREFIX + "CURRENT_USER";
	/**
	 * Request#attribute中的消息 */
	public static final String REQUEST_MESSAGE 	= KEY_PREFIX + "REQUEST_MESSAGE";
	
	// ---------------------------------------------- STATUS CODEs
	
	public static final int OK 		= 1000;
	public static final int FAIL 	= 1001;
	public static final int ERROR 	= 1002;
	
	/**
	 * @param request
	 * @param message
	 */
	public static void setMessage(ServletRequest request, String message) {
		request.setAttribute(REQUEST_MESSAGE, message);
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String getMessage(ServletRequest request) {
		return (String) request.getAttribute(WebUtils.REQUEST_MESSAGE);
	}
}
