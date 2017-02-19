package cn.devezhao.commons.web;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import cn.devezhao.commons.Bean2Json;

/**
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class RequestContextImpl implements RequestContext {

	volatile private HttpServletRequest request;
	volatile private HttpServletResponse response;

	public RequestContextImpl(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	public HttpSession getSession() {
		return request.getSession();
	}
	
	public Serializable getCurrentUser() {
		return (Serializable) getSession().getAttribute(WebUtils.CURRENT_USER);
	}
	
	public String getParameter(String paramName) {
		return getRequest().getParameter(paramName);
	}

	public void write(Object value) throws IOException {
		String source = (value == null) ? StringUtils.EMPTY : value.toString();
		response.getWriter().write(source);
	}

	public void writeJson(Object value) throws IOException {
		ServletUtils.setContentType(getResponse(), ServletUtils.CT_JSON_RFC4627);
		write(value);
	}
	
	public void writeStatus(int status, Object message) throws IOException {
		if (message != null) {
			writeJson(String.format("{status:%d, message:'%s'}", status, Bean2Json.escape(message)));
		} else {
			writeJson(String.format("{status:%d}", status));
		}
	}
	
	public void setMessage(String message) {
		if (message == null) {
			getRequest().removeAttribute(WebUtils.REQUEST_MESSAGE);
		} else {
			getRequest().setAttribute(WebUtils.REQUEST_MESSAGE, message);
		}
	}
}
