package cn.he.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.he.bookstore.user.domain.User;
import cn.he.utils.TxQueryRunner;

/**
 * User持久层
 * @author He
 *
 */
public class UserDao {

	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 按用户名查询
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) {
		String sql = "SELECT * FROM tb_user WHERE username=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class), username);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按邮箱查询
	 * @param username
	 * @return
	 */
	public User findByEmail(String email) {
		String sql = "SELECT * FROM tb_user WHERE email=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class), email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按激活码查询
	 * @param username
	 * @return
	 */
	public User findByCode(String code) {
		String sql = "SELECT * FROM tb_user WHERE code=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class), code);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 更改指定用户的激活状态
	 * @param uid
	 * @param state
	 */
	public void updateState(String uid, boolean state) {
		String sql = "UPDATE tb_user SET state=? WHERE uid=?";
		try {
			qr.update(sql, state, uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 插入用户数据
	 * @param user
	 */
	public void add(User user) {
		String sql = "INSERT INTO tb_user VALUES (?, ?, ?, ?, ?, ?)";
		Object[] params = {user.getUid(), user.getUsername(), user.getPassword(),
				user.getEmail(), user.getCode(), user.isState()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
