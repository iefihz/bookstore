package cn.he.bookstore.book.service;

import java.util.List;

import cn.he.bookstore.book.dao.BookDao;
import cn.he.bookstore.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();

	public List<Book> findAll() {
		return bookDao.findAll();
	}

	public List<Book> findByCategory(String cid) {
		return bookDao.findByCid(cid);
	}

	public Book load(String bid) {
		return bookDao.findByBid(bid);
	}

	public void add(Book book) {
		bookDao.add(book);
	}

	public void delete(String bid) {
		bookDao.delete(bid);
	}

	public void modify(Book book) {
		bookDao.modify(book);
	}
}
