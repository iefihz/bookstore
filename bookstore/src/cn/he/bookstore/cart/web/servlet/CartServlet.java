package cn.he.bookstore.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.book.domain.Book;
import cn.he.bookstore.book.service.BookService;
import cn.he.bookstore.cart.domain.Cart;
import cn.he.bookstore.cart.domain.CartItem;
import cn.he.utils.BaseServlet;

public class CartServlet extends BaseServlet {
	
	//往购物车添加商品条目
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取购物车
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		//获取条目、数量
		CartItem cartItem = new CartItem();
		int count = Integer.parseInt(request.getParameter("count"));
		Book book = new BookService().load(request.getParameter("bid"));
		cartItem.setBook(book);
		cartItem.setCount(count);
		
		//把条目添加到车
		cart.add(cartItem);
		
		return "forward:/jsps/cart/list.jsp";
	}
	
	//移除某一条目
	public String remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		cart.remove(request.getParameter("bid"));
		
		return "forward:/jsps/cart/list.jsp";
	}
	
	//清空购物车
	public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		cart.clear();
		
		return "forward:/jsps/cart/list.jsp";
	}
}
