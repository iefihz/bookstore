package cn.he.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

import cn.he.bookstore.book.domain.Book;
import cn.he.bookstore.category.domain.Category;
import cn.he.utils.CommonUtils;
import cn.he.utils.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	public List<Book> findAll() {
		String sql = "SELECT * FROM book WHERE del=false";
		try {
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//查询未删除的图书
	public List<Book> findByCid(String cid) {
		String sql = "SELECT * FROM book WHERE cid=? AND del=false";
		try {
			return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//查询任何图书
	public List<Book> findByCid(String cid, boolean flag) {
		String sql = "SELECT * FROM book WHERE cid=?";
		try {
			return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Book findByBid(String bid) {
		String sql = "SELECT * FROM book WHERE bid=?";
		try {
			Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
			Book book = CommonUtils.getBeanFromMap(map, Book.class);
			book.setCategory(CommonUtils.getBeanFromMap(map, Category.class));
			return book;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void add(Book book) {
		String sql = "INSERT INTO book VALUES (?, ?, ?, ?, ?, ?, ?)";
		Object[] params = {book.getBid(), book.getBname(), book.getPrice(),
				book.getAuthor(), book.getImage(), book.getCategory().getCid(),
				book.isDel()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//删除图书(假删除，事实上是修改del字段为true)
	public void delete(String bid) {
		String sql = "UPDATE book SET del=TRUE WHERE bid=?";
		try {
			qr.update(sql, bid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//修改图书
	public void modify(Book book) {
		String sql = "UPDATE book SET bname=?, price=?, author=?,"
				+ "image=?, cid=?, del=? WHERE bid=?";
		Object[] params = {book.getBname(), book.getPrice(),
				book.getAuthor(), book.getImage(), book.getCategory().getCid(),
				book.isDel(), book.getBid()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
