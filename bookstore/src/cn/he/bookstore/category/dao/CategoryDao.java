package cn.he.bookstore.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.he.bookstore.category.domain.Category;
import cn.he.utils.TxQueryRunner;

public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();

	//查找所有分类
	public List<Category> findAll() {
		String sql = "SELECT * FROM category";
		try {
			return qr.query(sql, new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//添加目录
	public void add(Category category) {
		String sql = "INSERT INTO category VALUES(?,?)";
		try {
			qr.update(sql, category.getCid(), category.getCname());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//删除分类
	public void remove(String cid) {
		String sql = "DELETE FROM category WHERE cid=?";
		try {
			qr.update(sql, cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//加载分类
	public Category findByCid(String cid) {
		String sql = "SELECT * FROM Category WHERE cid=?";
		try {
			return qr.query(sql, new BeanHandler<Category>(Category.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//修改分类
	public void modify(Category category) {
		String sql = "UPDATE Category SET cname=? WHERE cid=?";
		try {
			qr.update(sql, category.getCname(), category.getCid());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//按cname查找分类
	public Category findByCname(String cname) {
		String sql = "SELECT * FROM category WHERE cname=?";
		try {
			return qr.query(sql, new BeanHandler<Category>(Category.class), cname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
