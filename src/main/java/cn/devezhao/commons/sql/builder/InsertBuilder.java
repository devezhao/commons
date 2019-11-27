package cn.devezhao.commons.sql.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import cn.devezhao.commons.sql.SqlHelper;

/**
 * insert
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: InsertBuilder.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class InsertBuilder extends BaseBuilder {

	final private Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
	public InsertBuilder(String table) {
		super(table);
	}
	
	public InsertBuilder addColumn(String column) {
		return addColumn(column, ObjectUtils.NULL);
	}
	
	public InsertBuilder addColumn(String column, Object value) {
		if (value == null) {
			return this;
		}
		columns.put(column, value);
		return this;
	}
	
	public String toSql() {
		StringBuffer sql = new StringBuffer("insert into ");
		sql.append(SqlHelper.wrapIdent(getTable().toLowerCase())).append(" (");
		
		StringBuffer values = new StringBuffer(") values (");
		
		for (Map.Entry<String, Object> e : columns.entrySet()) {
			sql.append(SqlHelper.wrapIdent(e.getKey())).append(", ");
			
			Object v = e.getValue();
			if (ObjectUtils.NULL == v) {
				values.append("null");
			} else if (Number.class.isAssignableFrom(v.getClass())) {
				values.append(v);
			} else {
				values.append('\'').append(escapeSql(v)).append('\'');
			}
			values.append(", ");
		}
		int len  = sql.length();
		sql.delete(len - 2, len);
		len  = values.length();
		values.delete(len - 2, len);
		
		sql.append(values).append(")");
		return sql.toString();
	}
	
	public boolean hasColumn() {
		return columns.isEmpty();
	}
}
