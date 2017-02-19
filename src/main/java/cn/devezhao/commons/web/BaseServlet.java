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
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
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
