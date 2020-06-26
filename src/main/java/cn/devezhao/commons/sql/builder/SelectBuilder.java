package cn.devezhao.commons.sql.builder;

import cn.devezhao.commons.sql.SqlHelper;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * select
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: SelectBuilder.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class SelectBuilder extends ConditionalBuilder {

	final private List<String> columns = new LinkedList<>();
	
	private String orderBy;
	private String groupBy;
	private int limit = -1;
	private int offset = -1;
	
	public SelectBuilder(String table) {
		super(table);
	}
	
	public SelectBuilder addColumn(String column) {
		columns.add(column);
		return this;
	}
	
	public SelectBuilder addColumns(String[] columns) {
		Collections.addAll(this.columns, columns);
		return this;
	}
	
	public SelectBuilder addColumns(String column, String...columns) {
		this.columns.add(column);
		this.columns.addAll(Arrays.asList(columns));
		return this;
	}
	
	public SelectBuilder setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}
	
	public SelectBuilder setGroupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}
	
	public SelectBuilder setLimit(int limit) {
		return setLimit(limit, -1);
	}
	
	public SelectBuilder setLimit(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
		return this;
	}
	
	@Override
    public String toSql() {
		StringBuilder sql = new StringBuilder("select ");
		
		for (String c : columns) {
			if (c.contains("(")) {  // 函数
				sql.append(c);
			} else {
				sql.append(SqlHelper.wrapIdent(c));
			}
			sql.append(", ");
		}
		int len  = sql.length();
		sql.delete(len - 2, len);
		
		sql.append(" from ").append(SqlHelper.wrapIdent(table.toLowerCase())).append(" where (1 = 1)");
		if (whereClause != null) {
			sql.append(" and ").append(whereClause.toSql());
		}
		if (!StringUtils.isBlank(whereString)) {
			sql.append(" and ").append(whereString);
		}
		
		if (groupBy != null) {
			sql.append(" group by ").append(groupBy);
		}
		if (orderBy != null) {
			sql.append(" order by ").append(orderBy);
		}
		
		if (limit > 0) {
			sql.append(" limit ").append(limit);
			if (offset > -1) {
				sql.append(" offset ").append(offset);
			}
		}
		return sql.toString();
	}
	
	public boolean hasColumn() {
		return !this.columns.isEmpty();
	}
}
