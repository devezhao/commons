package cn.devezhao.commons.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

/**
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public interface RequestContext {
	
	HttpServletRequest getRequest();
	
	HttpServletResponse getResponse();
	
	HttpSession getSession();
	
	Serializable getCurrentUser();
	
	String getParameter(String paramName);
	
	int getIntParameter(String paramName);
	
	long getLongParameter(String paramName);
	
	double getDoubleParameter(String paramName);
	
	void write(Object value) throws IOException;
	
	void writeJSON(Object value) throws IOException;
	
	void writeStatus(int status, Object message) throws IOException;
}
