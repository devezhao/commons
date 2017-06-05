package cn.devezhao.commons.excel;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class ExcelReaderException extends NestableRuntimeException {
	private static final long serialVersionUID = 4004856977506652236L;

	public ExcelReaderException() {
		super();
	}

	public ExcelReaderException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ExcelReaderException(String msg) {
		super(msg);
	}

	public ExcelReaderException(Throwable cause) {
		super(cause);
	}
}
