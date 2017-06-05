package cn.devezhao.commons.excel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import cn.devezhao.commons.CalendarUtils;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class Cell {
	
	public static final Cell NULL = new Cell(null);
	
	private Object value;

	public Cell(Object value) {
		this.value = value;
	}
	
	public Object getRawValue() {
		return value;
	}
	
	public boolean isEmpty() {
		return value == null || StringUtils.isEmpty(value.toString());
	}
	
	public String asString() {
		return value == null ? null : value.toString();
	}
	
	public Integer asInt() {
		if (value == null) {
			return null;
		}
		if (value instanceof Integer) {
			return (Integer) value;
		}
		return NumberUtils.toInt(asString());
	}
	
	public Long asLong() {
		if (value == null) {
			return null;
		}
		if (value instanceof Long) {
			return (Long) value;
		}
		return NumberUtils.toLong(asString());
	}
	
	public Double asDouble() {
		if (value == null) {
			return null;
		}
		if (value instanceof Double) {
			return (Double) value;
		}
		return NumberUtils.toDouble(asString());
	}
	
	public Date asDate() {
		if (value == null) {
			return null;
		}
		if (value instanceof Date) {
			return (Date) value;
		}
		return CalendarUtils.parse(asString());
	}
	
	public Boolean asBool() {
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		return BooleanUtils.toBoolean(asString());
	}
	
	@Override
	public String toString() {
		if (value != null && value instanceof Date) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(value);
		}
		return this.asString();
	}
}
