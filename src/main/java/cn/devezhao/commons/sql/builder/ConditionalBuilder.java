package cn.devezhao.commons.sql.builder;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: ConditionalBuilder.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public abstract class ConditionalBuilder extends BaseBuilder {

	protected WhereClause whereClause;
	protected String whereString;

	public ConditionalBuilder(String table) {
		super(table);
	}

	public ConditionalBuilder setWhere(String where) {
		this.whereString = where;
		return this;
	}

	public ConditionalBuilder setWhere(WhereClause clause) {
		this.whereClause = clause;
		return this;
	}

	public WhereClause where() {
		if (whereClause == null) {
			whereClause = new WhereClause(this);
		}
		return whereClause;
	}
}
