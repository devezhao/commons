package cn.devezhao.commons.excel;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import cn.devezhao.commons.CalendarUtils;

/**
 * 单元格
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class Cell implements Serializable {
	private static final long serialVersionUID = -1590140578303295189L;

	/**
	 * 空单元格
	 */
	public static final Cell NULL = new Cell(null);
	
	private Object value;

	/**
	 * @param value
	 */
	public Cell(Object value) {
		this.value = value;
	}
	
	/**
	 * @return
	 */
	public Object getRawValue() {
		return value;
	}
	
	/**
	 * @return returns true if null or ''
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(asString());
	}
	
	public String asString() {
		return value == null ? null : value.toString();
	}
	
	/**
	 * @return
	 * @see NumberUtils#toInt(String)
	 */
	public Integer asInt() {
		if (isEmpty()) {
			return null;
		}
		if (value instanceof Integer) {
			return (Integer) value;
		}
		String istr = asString().replace(",", "");
		return NumberUtils.toInt(istr);
	}
	
	/**
	 * @return
	 * @see NumberUtils#toLong(String)
	 */
	public Long asLong() {
		if (isEmpty()) {
			return null;
		}
		if (value instanceof Long) {
			return (Long) value;
		}
		String istr = asString().replace(",", "");
		return NumberUtils.toLong(istr);
	}
	
	/**
	 * @return
	 * @see NumberUtils#toDouble(String)
	 */
	public Double asDouble() {
		if (isEmpty()) {
			return null;
		}
		if (value instanceof Double) {
			return (Double) value;
		}
		String istr = asString().replace(",", "");
		return NumberUtils.toDouble(istr);
	}
	
	/**
	 * @return
	 * @see BooleanUtils#toBoolean(String)
	 */
	public Boolean asBool() {
		if (isEmpty()) {
			return null;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		return BooleanUtils.toBoolean(asString());
	}
	
	/**
	 * @return
	 * @see CalendarUtils#parse(String)
	 * @see #asDate(String[])
	 */
	public Date asDate() {
		if (isEmpty()) {
			return null;
		}
		if (value instanceof Date) {
			return (Date) value;
		}
		
		String istr = asString();
		if (NumberUtils.isNumber(istr)) {
			double d = NumberUtils.toDouble(istr);
			return DateUtil.getJavaDate(d);
		} else {
			return CalendarUtils.parse(istr);
		}
	}
	
	/**
	 * @param formatDefines
	 * @return
	 */
	public Date asDate(String formatDefines[]) {
		if (isEmpty()) {
			return null;
		}
		if (value instanceof Date) {
			return (Date) value;
		}
		
		String istr = asString();
		for (String format : formatDefines) {
			Date d = CalendarUtils.parse(istr, format);
			if (d != null) {
				return d;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return asString();
	}
}
