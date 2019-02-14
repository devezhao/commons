package cn.devezhao.commons.sql.builder;

import org.apache.commons.lang.StringUtils;

import cn.devezhao.commons.sql.SqlHelper;

/**
 * delete
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: DeleteBuilder.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class DeleteBuilder extends ConditionalBuilder {

	public DeleteBuilder(String table) {
		super(table);
	}
	
	public String toSql() {
		StringBuffer sql = new StringBuffer("delete from ");
		sql.append(SqlHelper.wrapIdent(table.toLowerCase())).append(" where (1 = 1)");
		
		if (whereClause != null) {
			sql.append(" and ").append(whereClause.toSql());
		}
		if (!StringUtils.isBlank(whereString)) {
			sql.append(" and ").append(whereString);
		}
		return sql.toString();
	}
}
