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
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.devezhao.commons.ThrowableUtils;

/**
 * 统一的Servlet父类，便于统一处理
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: BaseServlet.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public abstract class BaseServlet extends HttpServlet implements RequestExecutor {
	private static final long serialVersionUID = -4379864218946482170L;

	private static final Log LOG = LogFactory.getLog(BaseServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp);
	}
	
	/**
	 * 执行 GET 或 POST
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestContext context = createRequestContext(request, response);
		try {
			long start = 0;
			if (LOG.isDebugEnabled()) {
				LOG.debug("Executing request: " + context);
				start = System.currentTimeMillis();
			}
			
			execute(context);
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Execute completed. Time of is: " + (System.currentTimeMillis() - start) + " ms.");
			}
			
		} catch (Exception ex) {
			handleException(context, ex);
		}
	}
	
	/**
	 * Logging Exception
	 */
	public void handleException(RequestContext context, Throwable ex) {
		Throwable cause = getRootCause(ex);
		LOG.error("Execute request failure!", cause);
		
		if (cause instanceof RuntimeException) {
			throw (RuntimeException) cause;
		}
		throw new RuntimeException(cause);
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 */
	protected RequestContext createRequestContext(
			HttpServletRequest request, HttpServletResponse response) {
		return new RequestContextImpl(request, response);
	}
	
	/**
	 * @param cause
	 * @return
	 */
	protected Throwable getRootCause(Throwable cause) {
		Throwable rootCause = null;
		if (cause == null) {
			rootCause = new RuntimeException("未知错误！");
		} else if (cause instanceof InvocationTargetException) {
			rootCause = ((InvocationTargetException) cause).getTargetException();
		} else {
			rootCause = ThrowableUtils.getRootCause(cause);
		}
		return rootCause;
	}
}
