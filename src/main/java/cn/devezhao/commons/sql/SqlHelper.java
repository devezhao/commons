package cn.devezhao.commons.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.cfg.C3P0Config;

import cn.devezhao.commons.ThrowableUtils;
import cn.devezhao.commons.sql.builder.WhereClause;

/**
 * Sql help methods
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: SqlHelper.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class SqlHelper {

	private static final Log LOG = LogFactory.getLog(SqlHelper.class);
	
	/**
	 * @return
	 */
	public static SqlHelper getInstance() {
		return _instance;
	}

	/**
	 * @return
	 */
	public Connection doGetConnection() {
		try {
			return comboPooledDataSource.getConnection();
		} catch (SQLException ex) {
			LOG.error("Could not get JDBC Connection!", ex);
			throw new DataAccessException("Could not get JDBC Connection!", ex);
		}
	}
	
	/**
	 * @return
	 */
	public static Connection getConnection() {
		return SqlHelper.getInstance().doGetConnection();
	}
	
	/**
	 * @param builder
	 * @return
	 */
	public static long executeInsert(Builder builder) {
		return executeInsert(builder.toSql());
	}
	
	/**
	 * @param asql
	 * @return
	 */
	public static long executeInsert(String asql) {
		Connection connect = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet keyRs = null;
		
		try {
			pstmt = connect.prepareStatement(asql, Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
			keyRs = pstmt.getGeneratedKeys();
			
			keyRs.next();
			return keyRs.getLong(1);
			
		} catch (SQLException ex) {
			LOG.error("Execute SQL(insert) failure!", ex);
			throw new DataAccessException("Execute SQL(insert) failure!", ex);
		} catch (Throwable ex) {
			LOG.error("Execute SQL(insert) unexception error!", ex);
			throw new DataAccessException("Execute SQL(insert) unexception error!", ex);
		} finally {
			SqlHelper.attemptClose(keyRs);
			attemptClose(pstmt);
			attemptClose(connect);
		}
	}
	
	/**
	 * @param builder
	 * @return
	 */
	public static int executeSql(Builder builder) {
		return executeSql(builder.toSql());
	}
	
	/**
	 * @param asql
	 * @return
	 */
	public static int executeSql(String asql) {
		Connection connect = getConnection();
		Statement stmt = null;
		
		try {
			stmt = connect.createStatement();
			return stmt.executeUpdate(asql);
		} catch (SQLException ex) {
			LOG.error("Execute SQL failure!", ex);
			throw new DataAccessException("Execute SQL failure!", ex);
		} catch (Throwable ex) {
			LOG.error("Execute SQL unexception error!", ex);
			throw new DataAccessException("Execute SQL unexception error!", ex);
		} finally {
			attemptClose(stmt);
			attemptClose(connect);
		}
	}
	
	/**
	 * @param builders
	 * @return
	 */
	public static int executeBtachSql(Builder[] builders) {
		String asqls[] = new String[builders.length];
		int idx = 0;
		for (Builder b : builders)
			asqls[idx++] = b.toSql();
		
		return executeBtachSql(asqls);
	}
	
	/**
	 * @param asqls
	 * @return
	 */
	public static int executeBtachSql(String[] asqls) {
		if (asqls.length == 1)
			return executeSql(asqls[0]);
		
		Connection connect = getConnection();
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			
			for (String asql : asqls)
				stmt.addBatch(asql);
			
			int[] affs = stmt.executeBatch();
			int aff = 0;
			for (int a : affs) aff += a;
			return aff;
		} catch (SQLException ex) {
			LOG.error("Execute SQL batch failure!", ex);
			throw new DataAccessException("Execute SQL batch failure!", ex);
		} catch (Throwable ex) {
			LOG.error("Execute SQL batch unexception error!", ex);
			throw new DataAccessException("Execute SQL batch unexception error!", ex);
		} finally {
			attemptClose(stmt);
			attemptClose(connect);
		}
	}
	
	/**
	 * @param builder
	 * @param inParameters
	 * @return
	 */
	public static Object[][] executeQuery(Builder builder, Object...inParameters) {
		return executeQuery(builder.toSql(), inParameters);
	}
	
	/**
	 * @param asql
	 * @param inParameters
	 * @return
	 */
	public static Object[][] executeQuery(String asql, Object...inParameters) {
		Connection connect = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Object[]> results = new LinkedList<Object[]>();
		try {
			pstmt = connect.prepareStatement(asql);
			if (inParameters.length > 0) {
				for (int i = 0; i < inParameters.length; i++) {
					pstmt.setObject(1 + i, inParameters[i]);
				}
			}
			rs = pstmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int colLen = rsmd.getColumnCount();
			while (rs.next()) {
				Object[] tmp = new Object[colLen];
				for (int i = 0; i < colLen; i++) {
					tmp[i] = rs.getObject(i + 1);
				}
				results.add(tmp);
			}
			
		} catch (SQLException ex) {
			LOG.error("Execute SQL(select) failure!", ex);
			throw new DataAccessException("Execute SQL(select) failure!", ex);
		} catch (Throwable ex) {
			LOG.error("Execute SQL(select) unexception error!", ex);
			throw new DataAccessException("Execute SQL(select) unexception error!", ex);
		} finally {
			attemptClose(rs);
			attemptClose(pstmt);
			attemptClose(connect);
		}
		
		return results.toArray(new Object[results.size()][]);
	}
	
	/**
	 * @param builder
	 * @param inParameters
	 * @return
	 */
	public static Object[] executeQueryUnique(Builder builder, Object...inParameters) {
		return executeQueryUnique(builder.toSql(), inParameters);
	}
	
	/**
	 * @param asql
	 * @param inParameters
	 * @return
	 */
	public static Object[] executeQueryUnique(String asql, Object...inParameters) {
		Connection connect = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = connect.prepareStatement(asql);
			if (inParameters.length > 0) {
				for (int i = 0; i < inParameters.length; i++) {
					pstmt.setObject(1 + i, inParameters[i]);
				}
			}
			rs = pstmt.executeQuery();
			rs.setFetchSize(1);
			
			Object[] tmp = null;
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colLen = rsmd.getColumnCount();
				tmp = new Object[colLen];
				
				for (int i = 0; i < colLen; i++) {
					tmp[i] = rs.getObject(i + 1);
				}
			}
			
			if (tmp == null)
				return null;
			return tmp;
		} catch (SQLException ex) {
			LOG.error("Execute SQL(select) failure!", ex);
			throw new DataAccessException("Execute SQL(select) failure!", ex);
		} catch (Throwable ex) {
			LOG.error("Execute SQL(select) unexception error!", ex);
			throw new DataAccessException("Execute SQL(select) unexception error!", ex);
		} finally {
			attemptClose(rs);
			attemptClose(pstmt);
			attemptClose(connect);
		}
	}
	
	/**
	 * @param table
	 * @return
	 */
	public static long count(String table) {
		return count(table, null);
	}
	
	/**
	 * @param table
	 * @param clause
	 * @return
	 */
	public static long count(String table, WhereClause clause) {
		StringBuffer asql = new StringBuffer("select count(*) from ");
		asql.append(wrapIdent(table.toLowerCase()));
		if (clause != null)
			asql.append(" where ").append(clause.toSql());
		
		Object[][] result = executeQuery(asql.toString());
		return (result == null || result.length == 0) ? 0 : (Long) result[0][0];
	}

	/**
	 * @param connect
	 */
	public static void attemptClose(Connection connect) {
		if (connect == null)
			return;
		try {
			if (connect.isClosed())
				return;
			connect.close();
		} catch (AbstractMethodError err) {
		} catch (SQLException sqlex) {
			LOG.error("Close Connection failure!", sqlex);
		}
	}

	/**
	 * @param stmt
	 */
	public static void attemptClose(Statement stmt) {
		if (stmt == null)
			return;
		try {
			if (stmt.isClosed())
				return;
			stmt.close();
		} catch (AbstractMethodError err) {
		} catch (SQLException sqlex) {
			LOG.error("Close Statement failure!", sqlex);
		}
	}

	/**
	 * @param rs
	 */
	public static void attemptClose(ResultSet rs) {
		if (rs == null)
			return;
		try {
			if (rs.isClosed())
				return;
			rs.close();
		} catch (AbstractMethodError err) {
		} catch (SQLException sqlex) {
			LOG.error("Close ResultSet failure!", sqlex);
		}
	}
	
	/**
	 * @param stmt
	 */
	public static void clear(Statement stmt) {
		if (stmt == null)
			return;
		try {
			if (stmt instanceof PreparedStatement)
				((PreparedStatement) stmt).clearParameters();
			stmt.clearBatch();
			stmt.clearWarnings();
		} catch (Throwable ex) {
			LOG.warn("Unable to clear JDBC Statement.");
		}
	}
	
	/**
	 * @param identifier
	 * @return
	 */
	public static String wrapIdent(String identifier) {
		return new StringBuffer("`").append(identifier).append('`').toString();
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String escapeSql(Object value) {
		if (value == null)
			return StringUtils.EMPTY;
		
		if (Number.class.isAssignableFrom(value.getClass()))
			return value.toString();
		return StringUtils.replace(value.toString(), "'", "''");
	}
	
	/**
	 * 是否约束错误
	 * 
	 * @param ex
	 * @return
	 */
	public static boolean isConstraintViolationException(Exception ex) {
		if (ex instanceof SQLIntegrityConstraintViolationException) {
			return true;
		}
		
		Throwable cause = ThrowableUtils.getRootCause(ex);
		if (cause instanceof SQLIntegrityConstraintViolationException) {
			return true;
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	static SqlHelper _instance;
	static {
		_instance = new SqlHelper();
	}
	
	private ComboPooledDataSource comboPooledDataSource;
	private SqlHelper() {
		try {
			C3P0Config.initializeUserOverridesAsString();
			comboPooledDataSource = new ComboPooledDataSource();
		} catch (Exception ex) {
			LOG.error("Initialize ComboPooledDataSource failure!!!", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
