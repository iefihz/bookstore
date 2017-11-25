package cn.he.bookstore.book.web.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.book.domain.Book;
import cn.he.bookstore.book.service.BookService;
import cn.he.bookstore.category.domain.Category;
import cn.he.bookstore.category.service.CategoryService;
import cn.he.utils.BaseServlet;
import cn.he.utils.CommonUtils;

public class AdminBookServlet extends BaseServlet {

	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	//查询所有图书
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		return "forward:/adminjsps/admin/book/list.jsp";
	}
	
	//按bid加载图书
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("book", bookService.load(request.getParameter("bid")));
		request.setAttribute("categoryList", categoryService.findAll());
		return "forward:/adminjsps/admin/book/desc.jsp";
	}
	
	//添加图书之前的加载所有分类
	public String addPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		return "forward:/adminjsps/admin/book/add.jsp";
	}
	
	//删除图书
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		bookService.delete(request.getParameter("bid"));
		return findAll(request, response);
	}
	
	//修改图书
	public String modify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Book book = CommonUtils.getBeanFromMap(request.getParameterMap(), Book.class);
		Category category = CommonUtils.getBeanFromMap(request.getParameterMap(), Category.class);
		book.setCategory(category);
		bookService.modify(book);
		return findAll(request, response);
	}
}
