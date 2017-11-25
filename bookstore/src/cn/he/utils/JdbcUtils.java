package cn.he.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcUtils {
	
	private static ThreadLocal<Connection> tL = new ThreadLocal<Connection>();

	/**
	 * 连接池的创建
	 */
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	
	/**
	 * 事务处理专用连接对象
	 */
//	private static Connection conn = null;
	
	/**
	 * 获取连接池里的链接对象
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = tL.get();
		if (null != conn) return conn;
		return dataSource.getConnection();
	}
	
	/**
	 * 获取连接池对象
	 * @return
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * 开启事务
	 * @throws SQLException 
	 */
	public static void beginTransaction() throws SQLException {
		Connection conn = tL.get();
		if (null != conn) throw new SQLException("已经开启了事务，不用重复开启！");
		conn = getConnection();
		conn.setAutoCommit(false);
		
		tL.set(conn);
	}
	
	/**
	 * 提交事务，设置conn = null的目的，表示事务结束，下次开启事务时，创建新的conn
	 * @throws SQLException 
	 */
	public static void commitTransaction() throws SQLException {
		Connection conn = tL.get();
		if (null == conn) throw new SQLException("事务还没开启，请勿提交！");
		conn.commit();
		conn.close();
		
//		conn = null;
		tL.remove();
	}
	
	/**
	 * 回滚事务，设置conn = null的目的，表示事务结束，下次开启事务时，创建新的conn
	 * @throws SQLException 
	 */
	public static void rollbackTransaction() throws SQLException {
		Connection conn = tL.get();
		if (null == conn) throw new SQLException("事务还没开启，请勿回滚！");
		conn.rollback();
		conn.close();
		
//		conn = null;
		tL.remove();
	}
	
	/**
	 * 判断是事务专用连接是否为空，如果为空则关闭传进来的连接，如果不为空，但不是事务
	 * 专用连接，那么也关闭传进来的连接
	 */
	public static void releaseConnection(Connection connection) {
		Connection conn = tL.get();
		try {
			if (null == conn) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != connection) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
