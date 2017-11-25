package cn.he.bookstore.user.web.servlet.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.user.domain.Admin;
import cn.he.utils.BaseServlet;
import cn.he.utils.CommonUtils;

public class AdminServlet extends BaseServlet {
	
	//管理员登录
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("admin-config.properties"));
		String adminName_true = props.getProperty("AdminName");
		String adminPassword_true = props.getProperty("AdminPassword");
		Admin admin = CommonUtils.getBeanFromMap(request.getParameterMap(), Admin.class);
		String adminName = admin.getAdminName();
		String adminPassword = admin.getAdminPassword();
		
		//账号用户名非空校验
		Map<String, String> errors = new HashMap<String, String>();
		if (null == adminName || adminName.trim().isEmpty()) {
			errors.put("err_adminName", "请输入管理员账户！");
		} else if (null == adminPassword || adminPassword.isEmpty()) {
			errors.put("err_adminPassword", "请输入管理员密码！");
		}
		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
			request.setAttribute("admin", admin);
			return "forward:/adminjsps/login.jsp";
		}
		
		if (!adminName_true.equals(adminName) ||
				!adminPassword_true.equals(adminPassword)) {
			request.setAttribute("msg", "管理员账号或密码错误！");
			request.setAttribute("admin", admin);
			return "forward:/adminjsps/login.jsp";
		} else {
			request.getSession().setAttribute("session_admin", admin);
			return "forward:/adminjsps/admin/index.jsp";
		}
		
	}
	
	//退出登录
	public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		return "redirect:/adminjsps/login.jsp";
	}
}
