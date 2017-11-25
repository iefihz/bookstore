package cn.he.bookstore.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.book.service.BookService;
import cn.he.utils.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	
	//查询所有图书
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		return "forward:/jsps/book/list.jsp";
	}

	//按Category查询图书
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findByCategory(request.getParameter("cid")));
		return "forward:/jsps/book/list.jsp";
	}
	
	//按bid查询图书详细信息
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookDesc", bookService.load(request.getParameter("bid")));
		return "forward:/jsps/book/desc.jsp";
	}
}
