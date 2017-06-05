package cn.devezhao.commons.web;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import cn.devezhao.commons.Bean2Json;
import cn.devezhao.commons.ObjectUtils;

/**
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
@SuppressWarnings("deprecation")
public class RequestContextImpl implements RequestContext {

	volatile private HttpServletRequest request;
	volatile private HttpServletResponse response;

	public RequestContextImpl(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public HttpServletResponse getResponse() {
		return response;
	}
	
	@Override
	public HttpSession getSession() {
		return request.getSession();
	}
	
	@Override
	public Serializable getCurrentUser() {
		return (Serializable) getSession().getAttribute(WebUtils.CURRENT_USER);
	}
	
	@Override
	public String getParameter(String paramName) {
		return getRequest().getParameter(paramName);
	}
	
	@Override
	public int getIntParameter(String paramName) {
		return ObjectUtils.toInt(getParameter(paramName));
	}
	
	@Override
	public long getLongParameter(String paramName) {
		return ObjectUtils.toLong(getParameter(paramName));
	}
	
	@Override
	public double getDoubleParameter(String paramName) {
		return ObjectUtils.toDouble(getParameter(paramName));
	}

	@Override
	public void write(Object value) throws IOException {
		String source = (value == null) ? StringUtils.EMPTY : value.toString();
		response.getWriter().write(source);
	}

	@Override
	public void writeJSON(Object value) throws IOException {
		ServletUtils.setContentType(getResponse(), ServletUtils.CT_JSON_RFC4627);
		write(value);
	}
	
	@Override
	public void writeStatus(int status, Object message) throws IOException {
		if (message != null) {
			writeJSON(String.format("{status:%d, message:\"%s\"}", status, Bean2Json.escape(message)));
		} else {
			writeJSON(String.format("{status:%d}", status));
		}
	}
}
