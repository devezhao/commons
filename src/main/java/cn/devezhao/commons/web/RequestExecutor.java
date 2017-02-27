package cn.devezhao.commons.web;

import java.io.Serializable;

/**
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public interface RequestExecutor extends Serializable {
	
	/**
	 * 执行请求
	 * 
	 * @param context
	 * @throws Exception
	 */
	void execute(RequestContext context) throws Exception;
	
	/**
	 * 异常处理
	 * 
	 * @param context
	 * @param ex
	 */
	void handleException(RequestContext context, Throwable ex);
}
