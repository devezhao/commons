package cn.devezhao.commons.sql;

import cn.devezhao.commons.sql.builder.*;

/**
 * Build select, insert, update, delete, where
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: SqlBuilder.java 6 2015-06-08 08:56:34Z zhaofang123@gmail.com $
 */
public class SqlBuilder {

	public static SelectBuilder buildSelect(String table) {
		return new SelectBuilder(table);
	}

	public static InsertBuilder buildInsert(String table) {
		return new InsertBuilder(table);
	}

	public static UpdateBuilder buildUpdate(String table) {
		return new UpdateBuilder(table);
	}

	public static DeleteBuilder buildDelete(String table) {
		return new DeleteBuilder(table);
	}
	
	public static WhereClause buildWhereClause() {
		return buildWhereClause(null);
	}
	
	public static WhereClause buildWhereClause(Builder parent) {
		return new WhereClause(parent);
	}
}
