package cn.devezhao.commons;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * @author Zhao Fangfang
 * @version $Id: Bean2Json.java 48 2015-08-18 02:57:54Z zhaofang123@g.com $
 * @since 1.0, 2013-7-2
 */
public class Bean2Json {

	/**
	 * @param map
	 * @return
	 */
	public static String toJson(Map<?, ?> map) {
		StringBuffer json = new StringBuffer("{");
		
		boolean first = true;
		for (Map.Entry<?, ?> e : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				json.append(",");
			}
			
			String k = e.getKey().toString();
			Object v = e.getValue();
			
			Class<?> vClazz = v == null ? String.class : v.getClass();
			if (Object[].class.isAssignableFrom(vClazz)) {
				json.append(wrapJson(k)).append(':').append(toJson((Object[]) v));
			} else if (Collection.class.isAssignableFrom(vClazz)) {
				Collection<?> innerArray = (Collection<?>) v;
				json.append(wrapJson(k)).append(':').append(toJson(innerArray));
			} else if (Map.class.isAssignableFrom(vClazz)) {
				@SuppressWarnings("unchecked")
				Map<Object, ?> innerMap = (Map<Object, ?>) v;
				json.append(wrapJson(k)).append(':').append(toJson(innerMap));
			} else {
				json.append(wrapJson(k)).append(':').append(wrapJson(v));
			}
		}
		
		json.append("}");
		return json.toString();
	}
	
	/**
	 * @param array
	 * @return
	 */
	public static String toJson(Collection<?> array) {
		return toJson(array.toArray(new Object[0]));
	}
	
	/**
	 * @param array
	 * @return
	 */
	public static String toJson(Object[] array) {
		StringBuffer json = new StringBuffer("[");
		
		boolean first = true;
		for (Object v : array) {
			if (first) {
				first = false;
			} else {
				json.append(",");
			}
			
			Class<?> vClazz = v == null ? null : v.getClass();
			if (vClazz != null && Object[].class.isAssignableFrom(vClazz)) {
				json.append(toJson((Object[]) v));
			} else if (vClazz != null && Collection.class.isAssignableFrom(vClazz)) {
				@SuppressWarnings("unchecked")
				Collection<Object> innerArray = (Collection<Object>) v;
				json.append(toJson(innerArray));
			} else if (vClazz != null && Map.class.isAssignableFrom(vClazz)) {
				@SuppressWarnings("unchecked")
				Map<Object, Object> innerMap = (Map<Object, Object>) v;
				json.append(toJson(innerMap));
			} else {
				json.append(wrapJson(v));
			}
		}
		
		json.append("]");
		return json.toString();
	}
	
	// ------
	
	private static final char DOT = '.';
	
	/**
	 * @param value
	 * @param escapeForce
	 * @return
	 */
	public static String wrapJson(Object value) {
		if (value == null || value.toString().length() < 1) {
			return "\"\"";
		}
		if (value instanceof String) {
			return '"' + escape(value) + '"';
		}
		
		String v = value.toString();
		if (isNumber(v) || "true".equals(v) || "false".equals(v)) {
			return v;
		}
		return '"' + escape(v) + '"';
	}
	
	/**
	 * @param json
	 * @return
	 */
	public static String escape(Object json) {
		if (json == null) {
			return StringUtils.EMPTY;
		}
		
		String jn = json.toString();
		if (jn.contains("&")) {
			jn = jn.replaceAll("&", "&amp;");
		}
		if (jn.contains("\"")) {
			jn = jn.replaceAll("\"", "&quot;");
		}
		jn = jn.replaceAll("[\\n\\r\\t]", "");
		if (jn.contains("\\")) {
			jn = jn.replaceAll("\\\\", "\\\\\\\\");
		}
		jn = jn.replace(" ", "");  // 一个奇怪的乱码
		return jn;
	}
	
	/**
	 * @param json
	 * @return
	 */
	public static String unescape(Object json) {
		if (json == null) {
			return StringUtils.EMPTY;
		}
		
		String str = json.toString();
		str = str.replaceAll("&quot;", "\"");
		str = str.replaceAll("&amp;", "&");
		return str;
	}
	
	/**
	 * @param v
	 * @return
	 * 
	 * @see NumberUtils#isDigits(String)
	 * @see NumberUtils#isNumber(String)
	 */
	public static boolean isNumber(String v) {
		if (StringUtils.isEmpty(v)) {
            return false;
        }
		
		if (v.charAt(0) == DOT || v.charAt(v.length() - 1) == DOT) {
        	return false;
        }
		
		int dot = 0;
        for (int i = 0; i < v.length(); i++) {
        	char ch = v.charAt(i);
            if (!Character.isDigit(ch)) {
            	if (ch != DOT) {
            		return false;
            	}
            	
            	dot++;
            	if (dot > 1) {
            		return false;
            	}
            }
        }
        return true;
	}
}
