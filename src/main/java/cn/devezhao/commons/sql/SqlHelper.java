package cn.devezhao.commons.sql;

import cn.devezhao.commons.ThrowableUtils;
import cn.devezhao.commons.sql.builder.WhereClause;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Sql help methods
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: SqlHelper.java 6 2015-06-08 08:56:34Z zhaofang123@gmail.com $
 */
public class SqlHelper {

	private static final Log LOG = LogFactory.getLog(SqlHelper.class);

	private static final SqlHelper instance = new SqlHelper();

	private SqlHelper() {}

	private final Object comboPooledDataSourceLock = new Object();
	private ComboPooledDataSource comboPooledDataSource;

	/**
	 * @return
	 */
	public static SqlHelper getInstance() {
		return instance;
	}

	/**
	 * @return
	 */
	public Connection doGetConnection() {
		if (comboPooledDataSource == null) {
			synchronized (comboPooledDataSourceLock) {
				if (comboPooledDataSource == null) {
					try {
						C3P0Config.initializeUserOverridesAsString();
						comboPooledDataSource = new ComboPooledDataSource();
					} catch (Exception ex) {
						LOG.error("Failed to initialize ComboPooledDataSource!", ex);
						throw new ExceptionInInitializerError(ex);
					}
				}
			}
		}

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
			LOG.error("Failed to execute SQL(insert)!", ex);
			throw new DataAccessException("Failed to execute SQL(insert)!", ex);
		} catch (Throwable ex) {
			LOG.error("Unexpected error executing SQL(insert)!", ex);
			throw new DataAccessException("Unexpected error executing SQL(insert)!", ex);
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
			LOG.error("Failed to execute SQL!", ex);
			throw new DataAccessException("Failed to execute SQL!", ex);
		} catch (Throwable ex) {
			LOG.error("Unexpected error executing SQL!", ex);
			throw new DataAccessException("Unexpected error executing SQL!", ex);
		} finally {
			attemptClose(stmt);
			attemptClose(connect);
		}
	}
	
	/**
	 * @param builders
	 * @return
	 */
	public static int executeBatchSql(Builder[] builders) {
		String[] sqls = new String[builders.length];
		int idx = 0;
		for (Builder b : builders) sqls[idx++] = b.toSql();
		return executeBatchSql(sqls);
	}
	
	/**
	 * @param asqls
	 * @return
	 */
	public static int executeBatchSql(String[] asqls) {
		if (asqls.length == 1) return executeSql(asqls[0]);
		
		Connection connect = getConnection();
		Statement stmt = null;
		try {
			stmt = connect.createStatement();
			
			for (String asql : asqls) stmt.addBatch(asql);
			
			int[] affs = stmt.executeBatch();
			int aff = 0;
			for (int a : affs) aff += a;
			return aff;
		} catch (SQLException ex) {
			LOG.error("Failed to execute SQL batch!", ex);
			throw new DataAccessException("Failed to execute SQL batch!", ex);
		} catch (Throwable ex) {
			LOG.error("Unexpected error executing SQL batch!", ex);
			throw new DataAccessException("Unexpected error executing SQL batch!", ex);
		} finally {
			attemptClose(stmt);
			attemptClose(connect);
		}
	}
	
	/**
	 * 在同一事务中执行多条 SQL，任一失败则整体回滚
	 * 
	 * @param asqls
	 * @return 影响行数合计
	 */
	public static int executeBatchSqlInTransaction(String[] asqls) {
		Connection connect = getConnection();
		Statement stmt = null;
		boolean oldAutoCommit = true;
		try {
			oldAutoCommit = connect.getAutoCommit();
			connect.setAutoCommit(false);
			
			stmt = connect.createStatement();
			for (String asql : asqls) stmt.addBatch(asql);
			
			int[] affs = stmt.executeBatch();
			int aff = 0;
			for (int a : affs) aff += a;
			
			connect.commit();
			return aff;
		} catch (Throwable ex) {
			try {
				connect.rollback();
			} catch (SQLException ignore) {
			}
			LOG.error("Failed to execute SQL batch(tx)!", ex);
			throw new DataAccessException("Failed to execute SQL batch(tx)!", ex);
		} finally {
			try {
				connect.setAutoCommit(oldAutoCommit);
			} catch (SQLException ignore) {
			}
			attemptClose(stmt);
			attemptClose(connect);
		}
	}
	
	/**
	 * @param builders
	 * @return
	 * @deprecated 拼写错误，请使用 {@link #executeBatchSql(Builder[])}
	 */
	@Deprecated
	public static int executeBtachSql(Builder[] builders) {
		return executeBatchSql(builders);
	}
	
	/**
	 * @param asqls
	 * @return
	 * @deprecated 拼写错误，请使用 {@link #executeBatchSql(String[])}
	 */
	@Deprecated
	public static int executeBtachSql(String[] asqls) {
		return executeBatchSql(asqls);
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
		
		List<Object[]> results = new LinkedList<>();
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
			LOG.error("Failed to execute SQL(select)!", ex);
			throw new DataAccessException("Failed to execute SQL(select)!", ex);
		} catch (Throwable ex) {
			LOG.error("Unexpected error executing SQL(select)!", ex);
			throw new DataAccessException("Unexpected error executing SQL(select)!", ex);
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

			return tmp;

		} catch (SQLException ex) {
			LOG.error("Failed to execute SQL(select)!", ex);
			throw new DataAccessException("Failed to execute SQL(select)!", ex);
		} catch (Throwable ex) {
			LOG.error("Unexpected error executing SQL(select)!", ex);
			throw new DataAccessException("Unexpected error executing SQL(select)!", ex);
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
		StringBuilder sql = new StringBuilder("select count(*) from ");
		sql.append(wrapIdent(table.toLowerCase()));
		if (clause != null) sql.append(" where ").append(clause.toSql());
		
		Object[][] result = executeQuery(sql.toString());
		return result.length == 0 ? 0 : ((Number) result[0][0]).longValue();
	}

	/**
	 * @param connect
	 */
	public static void attemptClose(Connection connect) {
		if (connect == null) return;
		try {
			if (connect.isClosed()) return;
			connect.close();
		} catch (AbstractMethodError ignore) {
		} catch (SQLException sqlex) {
			LOG.error("Failed to close Connection!", sqlex);
		}
	}

	/**
	 * @param stmt
	 */
	public static void attemptClose(Statement stmt) {
		if (stmt == null) return;
		try {
			if (stmt.isClosed()) return;
			stmt.close();
		} catch (AbstractMethodError ignore) {
		} catch (SQLException sqlex) {
			LOG.error("Failed to close Statement!", sqlex);
		}
	}

	/**
	 * @param rs
	 */
	public static void attemptClose(ResultSet rs) {
		if (rs == null) return;
		try {
			if (rs.isClosed()) return;
			rs.close();
		} catch (AbstractMethodError ignore) {
		} catch (SQLException sqlex) {
			LOG.error("Failed to close ResultSet!", sqlex);
		}
	}
	
	/**
	 * @param stmt
	 */
	public static void clear(Statement stmt) {
		if (stmt == null) return;
		try {
			if (stmt instanceof PreparedStatement) ((PreparedStatement) stmt).clearParameters();
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
		return "`" + identifier + '`';
	}
	
	/**
	 * 使用指定引号包裹标识符（{@link #wrapIdent(String)} 固定反引号，MySQL 方言）
	 * 
	 * @param identifier
	 * @param quote 标识符引号字符，如 '`' 或 '"'
	 * @return
	 */
	public static String wrapIdent(String identifier, char quote) {
		return quote + identifier + quote;
	}
	
	/**
	 * @param value
	 * @return
	 */
	public static String escapeSql(Object value) {
		if (value == null) return StringUtils.EMPTY;
		if (Number.class.isAssignableFrom(value.getClass())) return value.toString();
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
		return cause instanceof SQLIntegrityConstraintViolationException;
	}
}
