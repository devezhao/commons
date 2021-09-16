package cn.devezhao.commons.sql.builder;

import cn.devezhao.commons.sql.SqlHelper;
import org.apache.commons.lang.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * insert
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: InsertBuilder.java 6 2015-06-08 08:56:34Z zhaofang123@gmail.com $
 */
public class InsertBuilder extends BaseBuilder {

	final private Map<String, Object> columns = new LinkedHashMap<>();
	
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
	
	@Override
    public String toSql() {
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(SqlHelper.wrapIdent(getTable().toLowerCase())).append(" (");
		
		StringBuilder values = new StringBuilder(") values (");
		
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
		return !this.columns.isEmpty();
	}
}
