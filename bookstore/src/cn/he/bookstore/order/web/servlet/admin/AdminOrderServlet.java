package cn.he.bookstore.order.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.order.domain.Order;
import cn.he.bookstore.order.service.OrderService;
import cn.he.utils.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	
	private OrderService orderService = new OrderService();

	//所有订单
	public String allOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Order> orderList = orderService.allOrders();
		request.setAttribute("orderList", orderList);
		return "forward:/adminjsps/admin/order/list.jsp";
	}
	
	//按状态查询订单
	public String orders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Order> orderList = orderService.getOrdersByState(request.getParameter("state"));
		request.setAttribute("orderList", orderList);
		return "forward:/adminjsps/admin/order/list.jsp";
	}
	
	//发货
	public String deliver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		orderService.changeState(request.getParameter("oid"), 3);
		return orders(request, response);
	}
}
