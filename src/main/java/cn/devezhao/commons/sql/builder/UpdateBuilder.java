package cn.devezhao.commons.sql.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import cn.devezhao.commons.sql.SqlHelper;

/**
 * update
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: UpdateBuilder.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class UpdateBuilder extends ConditionalBuilder {

	final private Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
	public UpdateBuilder(String table) {
		super(table);
	}
	
	public UpdateBuilder addColumn(String column) {
		return addColumn(column, ObjectUtils.NULL);
	}
	
	public UpdateBuilder addColumn(String column, Object value) {
		if (value == null) {
			return this;
		}
		columns.put(column, value);
		return this;
	}
	
	@Override
    public String toSql() {
		StringBuffer sql = new StringBuffer("update ");
		sql.append(SqlHelper.wrapIdent(table.toLowerCase())).append(" set ");
		
		for (Map.Entry<String, Object> e : columns.entrySet()) {
			sql.append(SqlHelper.wrapIdent(e.getKey())).append(" = ");
			
			Object v = e.getValue();
			if (ObjectUtils.NULL == v) {
				sql.append("null");
			} else if (Number.class.isAssignableFrom(v.getClass())) {
				sql.append(v);
			} else {
				sql.append('\'').append(escapeSql(v)).append('\'');
			}
			sql.append(", ");
		}
		int len  = sql.length();
		sql.delete(len - 2, len);
		
		sql.append(" where (1 = 1)");
		if (whereClause != null) {
			sql.append(" and ").append(whereClause.toSql());
		}
		if (!StringUtils.isBlank(whereString)) {
			sql.append(" and ").append(whereString);
		}
		return sql.toString();
	}
	
	public boolean hasColumn() {
		return !this.columns.isEmpty();
	}
}
