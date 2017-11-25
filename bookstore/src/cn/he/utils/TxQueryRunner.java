package cn.he.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 * 重写QueryRunner参数不带Connection的方法，以供下次使用时，自动管理connection对象
 * @author He
 */
public class TxQueryRunner extends QueryRunner {

	@Override
	public int[] batch(String sql, Object[][] params) throws SQLException {
		/**
		 * 每个方法步骤都一样：
		 * 获取连接对象
		 * 执行父类方法
		 * 释放连接
		 * 返回值
		 */
		Connection conn = JdbcUtils.getConnection();
		int[] result = super.batch(conn, sql, params);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public int execute(String sql, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		int result = super.execute(conn, sql, params);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> List<T> execute(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		List<T> result = super.execute(conn, sql, rsh, params);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> T insert(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		T result = super.insert(conn, sql, rsh, params);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> T insert(String sql, ResultSetHandler<T> rsh) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		T result = super.insert(conn, sql, rsh);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> T insertBatch(String sql, ResultSetHandler<T> rsh, Object[][] params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		T result = super.insertBatch(conn, sql, rsh, params);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> T query(String sql, Object param, ResultSetHandler<T> rsh) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		T result = super.query(conn, sql, param, rsh);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> T query(String sql, Object[] params, ResultSetHandler<T> rsh) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		T result = super.query(conn, sql, params, rsh);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		T result = super.query(conn, sql, rsh, params);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		T result = super.query(conn, sql, rsh);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public int update(String sql, Object... params) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		int result = super.update(conn, sql, params);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public int update(String sql, Object param) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		int result = super.update(conn, sql, param);
		JdbcUtils.releaseConnection(conn);
		return result;
	}

	@Override
	public int update(String sql) throws SQLException {
		Connection conn = JdbcUtils.getConnection();
		int result = super.update(conn, sql);
		JdbcUtils.releaseConnection(conn);
		return result;
	}
	
}
