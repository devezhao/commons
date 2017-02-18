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

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: RequestContext.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public interface RequestContext {
	
	HttpServletRequest getRequest();
	
	HttpServletResponse getResponse();
	
	HttpSession getSession();
	
	Serializable getCurrentUser();
	
	String getParameter(String paramName);
	
	void write(Object value) throws IOException;
	
	void writeJson(Object value) throws IOException;
	
	void writeStatus(int status, Object message) throws IOException;
	
	void setMessage(String message);
}
