package cn.devezhao.commons.sql.builder;

import cn.devezhao.commons.sql.Builder;
import cn.devezhao.commons.sql.SqlHelper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;

/**
 * [...] where ...
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: WhereClause.java 6 2015-06-08 08:56:34Z zhaofang123@gmail.com $
 */
public class WhereClause implements Builder {
	
	private static final Log LOG = LogFactory.getLog(WhereClause.class);
	
	private final Builder parentBuilder;
	
	private final LinkedList<String> clauseQueue = new LinkedList<>();
	
	/**
	 * 1 = 谓词, 2 = 连接(or, and) */
	private int previousAppendItem = 2;
	
	public WhereClause() {
		this(null);
	}
	
	public WhereClause(Builder parent) {
		this.parentBuilder = parent;
	}
	
	public WhereClause eq(String column, Object value) {
		addToClauseQueue(column, "=", value);
		return this;
	}
	
	public WhereClause gt(String column, Object value) {
		addToClauseQueue(column, ">", value);
		return this;
	}
	
	public WhereClause lt(String column, Object value) {
		addToClauseQueue(column, "<", value);
		return this;
	}
	
	public WhereClause isnull(String column) {
		addToClauseQueue(column, "is", ObjectUtils.NULL);
		return this;
	}
	
	public WhereClause isnotnull(String column) {
		addToClauseQueue(column, "is not", ObjectUtils.NULL);
		return this;
	}
	
	public WhereClause like(String column, Object value) {
		addToClauseQueue(column, "like", value);
		return this;
	}
	
	public WhereClause and() {
		if (previousAppendItem == 2) {
			LOG.warn("Previous appended item is a join item (or/and)");
		}
		previousAppendItem = 2;
		
		clauseQueue.add("and");
		return this;
	}
	
	public WhereClause or() {
		if (previousAppendItem == 2) {
			LOG.warn("Previous appended item is a join item (or/and)");
		}
		previousAppendItem = 2;
		
		clauseQueue.add("or");
		return this;
	}
	
	@Override
    public String toSql() {
		StringBuilder sql = new StringBuilder("( ");
		for (String s : clauseQueue) {
			sql.append(s).append(' ');
		}
		return sql.append(")").toString();
	}
	
	public String toFullSql() {
		return (parentBuilder != null) ? parentBuilder.toSql() : toSql();
	}
	
	private void addToClauseQueue(String column, String operate, Object v) {
		if (previousAppendItem == 1) {
			LOG.warn("Previous appended item is a predicate");
		}
		previousAppendItem = 1;
		
		clauseQueue.add(SqlHelper.wrapIdent(column));
		clauseQueue.add(operate);
		
		if (v == null) {
			clauseQueue.add("?");
		} else if (ObjectUtils.NULL == v) {
			clauseQueue.add("null");
		} else if (Number.class.isAssignableFrom(v.getClass())) {
			clauseQueue.add(v.toString());
		} else {
			clauseQueue.add('\'' + SqlHelper.escapeSql(v) + '\'');
		}
	}
}