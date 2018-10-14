package cn.devezhao.commons.web;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

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
	
	/**
	 * @param request
	 * @return
	 */
	public static String dumpHeaders(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		Enumeration<?> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getHeader(name);
			sb.append(name).append('=').append(StringUtils.isBlank(value) ? "<blank>" : value).append("; ");
		}
		return sb.toString();
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String dumpAttributes(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
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
		StringBuffer sb = new StringBuffer();
		Enumeration<?> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			Object value = request.getParameter(name);
			sb.append(name).append('=').append(value == null ? "<null>" : value).append("; ");
		}
		return sb.toString();
	}
}
