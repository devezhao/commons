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
package cn.devezhao.commons;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * 异常对象工具
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: ThrowableUtils.java 99 2012-09-07 17:05:19Z zhaofang123@gmail.com $
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
			
			String stack = writer.toString();
			return stack;
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
