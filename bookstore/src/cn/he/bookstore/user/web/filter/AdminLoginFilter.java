package cn.he.bookstore.user.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.he.bookstore.user.domain.Admin;

/**
 * 管理员登录过滤器
 * @author He
 *
 */
public class AdminLoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Admin admin = (Admin) httpRequest.getSession().getAttribute("session_admin");
		if (null == admin) {
			httpRequest.setAttribute("msg", "您还没有登录，不能使用管理员权限！");
			httpRequest.getRequestDispatcher("/adminjsps/login.jsp").forward(httpRequest, response);
		} else {
			fChain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
	
}
