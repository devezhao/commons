package cn.devezhao.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Zhao Fangfang
 * @version $Id: DateFormatUtils.java 101 2012-09-11 15:24:46Z zhaofang123@gmail.com $
 * @since 2.6, 2012-9-11
 */
public class DateFormatUtils {

	public static final Locale DEFAULT_LOCALE = Locale.CHINESE;
	
	/**
	 * 获取日志格式<tt>yyyy年MM月dd日</tt>
	 * 
	 * @return
	 */
	public static DateFormat getCNDateFormat() {
		return CN_DATE_FORMAT_TL.get();
	}
	
	/**
	 * 获取日志格式<tt>HH时mm分ss秒</tt>
	 * 
	 * @return
	 */
	public static DateFormat getCNTimeFormat() {
		return CN_TIME_FORMAT_TL.get();
	}
	
	/**
	 * 获取日志格式<tt>yyyy年MM月dd日 HH时mm分ss秒</tt>
	 * 
	 * @return
	 */
	public static DateFormat getCNDateTimeFormat() {
		return CN_DATETIME_FORMAT_TL.get();
	}
	
	/**
	 * 获取日志格式<tt>yyyy-MM-dd</tt>
	 * 
	 * @return
	 */
	public static DateFormat getUTCDateFormat() {
		return UTC_DATE_FORMAT_TL.get();
	}
	
	/**
	 * 获取日志格式<tt>yyyy-MM-dd HH:mm:ss</tt>
	 * 
	 * @return
	 */
	public static DateFormat getUTCDateTimeFormat() {
		return UTC_DATETIME_FORMAT_TL.get();
	}
	
	/**
	 * 获取日志格式<tt>yyyyMMdd</tt>
	 * 
	 * @return
	 */
	public static DateFormat getPlainDateFormat() {
		return PLAIN_DATE_FORMAT_TL.get();
	}
	
	/**
	 * 获取日志格式<tt>yyyyMMddHHmmss</tt>
	 * 
	 * @return
	 */
	public static DateFormat getPlainDateTimeFormat() {
		return PLAIN_DATETIME_FORMAT_TL.get();
	}
	
	/**
	 * 获取指定格式的日志格式化对象
	 * 
	 * @return
	 */
	public static DateFormat getDateFormat(String formatted) {
		return new SimpleDateFormat(formatted, DEFAULT_LOCALE);
	}
	
	public static final String CN_DATE_FORMAT = "yyyy年MM月dd日";
	private static final ThreadLocal<DateFormat> CN_DATE_FORMAT_TL = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() { return new SimpleDateFormat(CN_DATE_FORMAT); }
	};
	
	public static final String CN_TIME_FORMAT = "HH时mm分ss秒";
	private static final ThreadLocal<DateFormat> CN_TIME_FORMAT_TL = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() { return new SimpleDateFormat(CN_TIME_FORMAT); }
	};
	
	public static final String CN_DATETIME_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
	private static final ThreadLocal<DateFormat> CN_DATETIME_FORMAT_TL = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() { return new SimpleDateFormat(CN_DATETIME_FORMAT); }
	};
	
	public static final String UTC_DATE_FORMAT = "yyyy-MM-dd";
	private static final ThreadLocal<DateFormat> UTC_DATE_FORMAT_TL = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() { return new SimpleDateFormat(UTC_DATE_FORMAT); }
	};
	
	public static final String UTC_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final ThreadLocal<DateFormat> UTC_DATETIME_FORMAT_TL = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() { return new SimpleDateFormat(UTC_DATETIME_FORMAT); }
	};
	
	public static final String PLAIN_DATE_FORMAT = "yyyyMMdd";
	private static final ThreadLocal<DateFormat> PLAIN_DATE_FORMAT_TL = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() { return new SimpleDateFormat(PLAIN_DATE_FORMAT); }
	};
	
	public static final String PLAIN_DATETIME_FORMAT = "yyyyMMddHHmmss";
	private static final ThreadLocal<DateFormat> PLAIN_DATETIME_FORMAT_TL = new ThreadLocal<DateFormat>(){
		protected DateFormat initialValue() { return new SimpleDateFormat(PLAIN_DATETIME_FORMAT); }
	};
}
