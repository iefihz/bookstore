package cn.he.bookstore.category.web.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.category.domain.Category;
import cn.he.bookstore.category.service.CategoryException;
import cn.he.bookstore.category.service.CategoryService;
import cn.he.utils.BaseServlet;
import cn.he.utils.CommonUtils;

public class AdminCategoryServlet extends BaseServlet {

	CategoryService categoryService = new CategoryService();
	
	//查询所有分类
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		return "forward:/adminjsps/admin/category/list.jsp";
	}

	//添加分类
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category category = new Category();
		category.setCid(CommonUtils.getUUID());
		category.setCname(request.getParameter("cname"));
		categoryService.addCategory(category);
		return findAll(request, response);
	}
	
	//删除分类
	public String remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			categoryService.remove(request.getParameter("cid"));
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "forward:/adminjsps/msg.jsp";
		}
		return findAll(request, response);
	}
	
	//预修改分类(加载)
	public String preModify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category category = categoryService.preModify(request.getParameter("cid"));
		request.setAttribute("category", category);
		return "forward:/adminjsps/admin/category/mod.jsp";
	}
	
	//修改分类
	public String modify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cname = request.getParameter("cname");
		String cid = request.getParameter("cid");
		Category category = CommonUtils.getBeanFromMap(request.getParameterMap(),
				Category.class);
		try {
			categoryService.modify(category);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "forward:/adminjsps/msg.jsp";
		}
		return findAll(request, response);
	}
}
