package cn.devezhao.commons;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 对象处理相关
 * 
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@gmail.com $
 */
public class ObjectUtils {
	
	/**
	 * @param o
	 * @return
	 */
	public static boolean toBool(Object o) {
		return toBool(o, Boolean.FALSE);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see BooleanUtils#toBoolean(String)
	 */
	public static boolean toBool(Object o, boolean defaultVal) {
		if (o == null) {
			return defaultVal;
		} else if (o instanceof Boolean) {
			return (Boolean) o;
		}
		return BooleanUtils.toBoolean(o.toString());
	}

	/**
	 * @param o
	 * @return
	 */
	public static int toInt(Object o) {
		return toInt(o, 0);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see NumberUtils#toInt(String)
	 */
	public static int toInt(Object o, int defaultVal) {
		if (o == null) {
			return defaultVal;
		}
		if (o instanceof Number) {
			return ((Number) o).intValue();
		}
		return NumberUtils.toInt(o.toString());
	}

	/**
	 * @param o
	 * @return
	 */
	public static long toLong(Object o) {
		return toLong(o, 0L);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see NumberUtils#toLong(String)
	 */
	public static long toLong(Object o, long defaultVal) {
		if (o == null) {
			return defaultVal;
		}
		if (o instanceof Number) {
			return ((Number) o).longValue();
		}
		return NumberUtils.toLong(o.toString());
	}

	/**
	 * @param o
	 * @return
	 */
	public static double toDouble(Object o) {
		return toDouble(o, 0d);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see NumberUtils#toDouble(String)
	 */
	public static double toDouble(Object o, double defaultVal) {
		if (o == null) {
			return defaultVal;
		}
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}
		return NumberUtils.toDouble(o.toString());
	}
	
	/**
	 * 比较两个对象
	 * 
	 * @param object
	 * @param another
	 * @return
	 * @see Object#equals(Object)
	 */
	public static boolean equals(Object object, Object another) {
		if (object == null && another == null) return true;
		if (object == null || another == null) return false;
		return object.equals(another);
	}
	
	/**
	 * 保留指定精度（四舍五入）
	 * 
	 * @param value
	 * @param scale 小数位
	 * @return
	 */
	public static double round(double value, int scale) {
		return BigDecimal.valueOf(value)
				.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * 除法，两个数字中任意一个为0则返回0
	 * 
	 * @param divisor
	 * @param dividend
	 * @return
	 */
	public static double divide(int divisor, int dividend) {
		return divide(divisor + 0d, dividend + 0d);
	}
	
	/**
	 * 除法，两个数字中任意一个为0则返回0
	 * 
	 * @param divisor
	 * @param dividend
	 * @return
	 */
	public static double divide(double divisor, double dividend) {
		if (divisor == 0 || dividend == 0) return 0d;
		return divisor / dividend;
	}
}
