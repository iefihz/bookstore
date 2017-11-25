package cn.he.utils;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/**
		 * 解决了post请求乱码问题
		 */
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String methodName = request.getParameter("method");
		if (null == methodName || methodName.trim().isEmpty())
			throw new RuntimeException("没有名为method的参数");
		Method method = null;
		try {
			method = this.getClass().getDeclaredMethod(methodName,
					HttpServletRequest.class, HttpServletResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String msg = (String) method.invoke(this, request, response);
			if (null == msg || msg.trim().isEmpty()) return;
			if (msg.contains(":")) {
				int index = msg.indexOf(":");
				String type = msg.substring(0, index);
				String src = msg.substring(index+1);
	
				if (type.trim().isEmpty() || null == type ||
						type.trim().equals("forward")) {
					request.getRequestDispatcher(src).forward(request, response);
				} else if (type.equals("include")) {
					request.getRequestDispatcher(src).include(request, response);
				} else if (type.equals("redirect")) {
					response.sendRedirect(request.getContextPath() + src);
				} else {
					response.getWriter().write("该版本还不支持：" + type);
				}
			} else {
				request.getRequestDispatcher(msg).forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
