package cn.he.bookstore.category.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.category.service.CategoryService;
import cn.he.utils.BaseServlet;

public class CategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		return "forward:/jsps/left.jsp";
	}
}
