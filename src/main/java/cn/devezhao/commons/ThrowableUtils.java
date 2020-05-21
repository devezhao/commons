package cn.devezhao.commons;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * 异常对象工具
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public final class ThrowableUtils {

	/**
	 * 获取原始异常对象
	 * 
	 * @param ex
	 * @return
	 */
	public static Throwable getRootCause(Throwable ex) {
		if (ex == null) {
			throw new RuntimeException("Unknow exception");
		}
		
		Throwable cause = ExceptionUtils.getRootCause(ex);
		return (cause == null ? ex : cause);
	}

	/**
	 * 提取错误堆栈
	 * 
	 * @param ex
	 * @return
	 */
	public static String extractStackTrace(Throwable ex) {
		if (ex == null) {
			return "<no cause>";
		}

		StringWriter writer = null;
		PrintWriter print = null;
		try {
			writer = new StringWriter();
			print = new PrintWriter(writer);
			ex.printStackTrace(print);
			print.flush();

            return writer.toString();
		} finally {
			try {
				print.close();
			} catch (Throwable ex1){};
			try {
				writer.close();
			} catch (Throwable ex2){};
		}
	}
}
