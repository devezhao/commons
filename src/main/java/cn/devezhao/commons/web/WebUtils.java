/*
 Copyright (C) 2010 QDSS.org
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.devezhao.commons.web;

import javax.servlet.ServletRequest;

/**
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: WebUtils.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
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
