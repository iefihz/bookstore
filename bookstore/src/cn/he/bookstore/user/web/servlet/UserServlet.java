package cn.he.bookstore.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.cart.domain.Cart;
import cn.he.bookstore.user.domain.User;
import cn.he.bookstore.user.service.UserException;
import cn.he.bookstore.user.service.UserService;
import cn.he.utils.BaseServlet;
import cn.he.utils.CommonUtils;
import cn.he.utils.MailUtils;

/**
 * User表述层
 * @author He
 *
 */
public class UserServlet extends BaseServlet {
	
	private UserService userService = new UserService();
	
	//注册
	public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User form = CommonUtils.getBeanFromMap(request.getParameterMap(), User.class);
		form.setUid(CommonUtils.getUUID());
		form.setCode(CommonUtils.getUUID()+CommonUtils.getUUID());
		
		/**
		 * 表单校验，用户名、密码、邮箱
		 */
		Map<String, String> errors = new HashMap<String, String>();
		String username = form.getUsername();
		if (null == username || username.trim().isEmpty()) {
			errors.put("er_username", "用户名不能为空！");
		} else if (username.length() < 2 || username.length() > 10) {
			errors.put("er_username", "用户名长度只能为2~10位！");
		} else if (username.startsWith(" ") || username.endsWith(" ")) {
			errors.put("er_username", "用户名始末不能为空格！");
		}
		String password = form.getPassword();
		if (null == password || password.trim().isEmpty()) {
			errors.put("er_password", "密码不能为空！");
		} else if (password.length() < 3 || password.length() > 15) {
			errors.put("er_password", "密码长度只能为3~15位！");
		}
		String email = form.getEmail();
		if (null == email || email.trim().isEmpty()) {
			errors.put("er_email", "邮箱不能为空！");
		} else if (!email.matches("\\w+@\\w+\\.\\w+")) {
			errors.put("er_email", "邮箱格式不正确！");
		}
		if (0 < errors.size()) {
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "forward:/jsps/user/regist.jsp";
		}
		
		try {
			userService.regist(form);
			
		} catch (UserException e) {
			
			//校验用户名、邮箱后的异常信息
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "forward:/jsps/user/regist.jsp";
		}
		
		/**
		 * 发邮件
		 */
		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream("/email-config.properties"));
		String host_ = props.getProperty("host");
		String username_ = props.getProperty("username");
		String password_ = props.getProperty("password");
		String from_ = props.getProperty("from");
		String to_ = form.getEmail();
		String subject_ = props.getProperty("subject");
		String content_ = props.getProperty("content");
		content_ = MessageFormat.format(content_, form.getCode());//替换{0}
		MailUtils.sendMails(host_, username_, password_, from_, to_, subject_, content_, null);
		
		request.setAttribute("msg", "恭喜您，已注册成功！请到邮箱激活");
		return "forward:/jsps/msg.jsp";
	}
	
	//激活
	public String active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			userService.active(request.getParameter("code"));
			request.setAttribute("msg", "激活成功！请登录！");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "forward:/jsps/msg.jsp";
	}
	
	//登录
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User form = CommonUtils.getBeanFromMap(request.getParameterMap(), User.class);
		
		Map<String, String> errors = new HashMap<String, String>();
		if (null == form.getUsername() || form.getUsername().trim().isEmpty()) {
			errors.put("er_uname", "请填写用户名！");
		}
		if (null == form.getPassword() || form.getPassword().trim().isEmpty()) {
			errors.put("er_pwd", "请填写密码！");
		}
		if (errors.size() > 0) {
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "forward:/jsps/user/login.jsp";
		}
		
		try {
			User user = userService.login(form);
			request.getSession().setAttribute("session_user", user);
			
			//登录成功，生成购物车到Session
			request.getSession().setAttribute("cart", new Cart());
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "forward:/jsps/user/login.jsp";
		}
		
		return "redirect:/index.jsp";
	}
	
	//退出
	public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		return "redirect:/index.jsp";
	}
}
