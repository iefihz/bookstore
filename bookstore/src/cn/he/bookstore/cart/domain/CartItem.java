package cn.he.bookstore.cart.domain;

import java.math.BigDecimal;

import org.junit.Test;

import cn.he.bookstore.book.domain.Book;

/**
 * 购物车条目
 * @author He
 *
 */
public class CartItem {
	private Book book;
	private int count;
	
	//小计
	public double getSubtotal() {
		BigDecimal bprice = new BigDecimal(Double.toString(book.getPrice()));
		BigDecimal bcount = new BigDecimal(Double.toString(count));
		return bprice.multiply(bcount).doubleValue();
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
