package cn.devezhao.commons.http4;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 执行Http方法失败
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class ExecuteHttpMethodException extends NestableRuntimeException {
	private static final long serialVersionUID = 1350464686207313368L;

	public ExecuteHttpMethodException(String message) {
		super(message);
	}

	public ExecuteHttpMethodException(Throwable ex) {
		super(ex);
	}

	public ExecuteHttpMethodException(String message, Throwable ex) {
		super(message, ex);
	}
}
