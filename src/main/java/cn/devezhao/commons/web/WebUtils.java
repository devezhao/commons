package cn.devezhao.commons.web;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

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
	 * KEY 前缀  */
	public static final String KEY_PREFIX 		= WebUtils.class.getPackage().getName() + "__";
	/**
	 * 当前会话中的用户 */
	public static final String CURRENT_USER 	= KEY_PREFIX + "CURRENT_USER";
	/**
	 * Request#attribute 中的消息 */
	public static final String REQUEST_MESSAGE 	= KEY_PREFIX + "REQUEST_MESSAGE";
	
	// ---------------------------------------------- STATUS CODEs
	
	public static final int STATUS_OK 		= 0;
	public static final int STATUS_FAIL 	= 1;
	public static final int STATUS_ERROR 	= 2;
	
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
	
	/** 敏感请求头（值不予明文输出，避免凭据泄露到日志） */
	private static final String[] SENSITIVE_HEADERS = {
			"cookie", "set-cookie", "authorization", "proxy-authorization", "x-auth-token", "x-api-key" };

	/**
	 * 输出所有请求头。其中 Cookie、Authorization 等敏感头的值会被脱敏为 <tt>&lt;masked&gt;</tt>
	 *
	 * @param request
	 * @return
	 */
	public static String dumpHeaders(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Enumeration<?> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getHeader(name);
			if (isSensitiveHeader(name)) {
				value = "<masked>";
			} else {
				value = StringUtils.isBlank(value) ? "<blank>" : value;
			}
			sb.append(name).append('=').append(value).append("; ");
		}
		return sb.toString();
	}

	/**
	 * @param name
	 * @return
	 */
	private static boolean isSensitiveHeader(String name) {
		for (String sensitive : SENSITIVE_HEADERS) {
			if (sensitive.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String dumpAttributes(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Enumeration<?> names = request.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			Object value = request.getAttribute(name);
			sb.append(name).append('=').append(value == null ? "<null>" : value).append("; ");
		}
		return sb.toString();
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String dumpParameters(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Enumeration<?> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			Object value = request.getParameter(name);
			sb.append(name).append('=').append(value == null ? "<null>" : value).append("; ");
		}
		return sb.toString();
	}
}
