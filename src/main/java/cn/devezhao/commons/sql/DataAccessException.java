package cn.devezhao.commons.sql;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: DataAccessException.java 6 2015-06-08 08:56:34Z zhaofang123@gmail.com $
 */
public class DataAccessException extends NestableRuntimeException {
	private static final long serialVersionUID = 2114595356855914344L;

	public DataAccessException() {
		super();
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable ex) {
		super(ex);
	}

	public DataAccessException(String message, Throwable ex) {
		super(message, ex);
	}
}
