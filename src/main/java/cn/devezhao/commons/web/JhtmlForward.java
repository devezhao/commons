package cn.devezhao.commons.web;

import cn.devezhao.commons.ThrowableUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 页面转发，如果不想暴露为 .JSP 页面
 * 
 * @author Zhao Fangfang
 * @version $Id: JhtmlForward.java 110 2013-06-22 09:40:22Z zhaofang123@gmail.com $
 * @since 2.6, 2012-9-14
 */
public class JhtmlForward extends HttpServlet {
	private static final long serialVersionUID = -6821655106468381401L;
	
	protected String contextPath;
	protected String prefix;
	protected String suffix;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		contextPath = config.getServletContext().getContextPath();
		prefix = StringUtils.defaultIfEmpty(config.getInitParameter("prefix"), "");
		suffix = StringUtils.defaultIfEmpty(config.getInitParameter("suffix"), ".htm");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String toUrl = request.getRequestURI();
		toUrl = toUrl.replaceFirst(contextPath, prefix);
		toUrl = toUrl.replace(suffix, ".jsp");
		
		try {
			ServletUtils.forward(request, response, toUrl);
		} catch (Exception ex) {
			Throwable root = ThrowableUtils.getRootCause(ex);
			if (root instanceof FileNotFoundException) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, root.getLocalizedMessage());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, root.getLocalizedMessage());
			}
		}
	}
}
