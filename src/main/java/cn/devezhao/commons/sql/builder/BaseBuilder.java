package cn.devezhao.commons.sql.builder;

import cn.devezhao.commons.sql.Builder;
import cn.devezhao.commons.sql.SqlHelper;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: BaseBuilder.java 6 2015-06-08 08:56:34Z zhaofang123@gmail.com $
 */
public abstract class BaseBuilder implements Builder {
	
	final protected String table;
	
	protected BaseBuilder(String table) {
		this.table = table;
	}
	
	public String getTable() {
		return table;
	}
	
	protected String escapeSql(Object o) {
		return SqlHelper.escapeSql(o);
	}
}
