package cn.he.bookstore.order.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.he.bookstore.cart.domain.Cart;
import cn.he.bookstore.cart.domain.CartItem;
import cn.he.bookstore.order.domain.Order;
import cn.he.bookstore.order.domain.OrderItem;
import cn.he.bookstore.order.service.OrderException;
import cn.he.bookstore.order.service.OrderService;
import cn.he.bookstore.user.domain.User;
import cn.he.utils.BaseServlet;
import cn.he.utils.CommonUtils;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	/**
	 * 一个订单对应一个购物车，一组订单条目对应一组购物车条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Order order = new Order();
		order.setOid(CommonUtils.getUUID());
		order.setOrdertime(new Date());
		order.setState(1);
//		order.setAddress("");
		User owner = (User) request.getSession().getAttribute("session_user");
		order.setOwner(owner);
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		//购物车为空
		if (null == cart.getCartItems() || 0 == cart.getCartItems().size()) {
			return "forward:/jsps/cart/list.jsp";
		}
		
		order.setTotal(cart.getTotal());

		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem oi = new OrderItem();
			oi.setBook(cartItem.getBook());
			oi.setCount(cartItem.getCount());
			oi.setIid(CommonUtils.getUUID());
			oi.setOrder(order);
			oi.setSubtotal(cartItem.getSubtotal());
			orderItemList.add(oi);
		}
		order.setOrderItemLists(orderItemList);
		
		orderService.add(order);
		
		//清空购物车
		cart.clear();
		
		request.setAttribute("order", order);
		
		return "forward:/jsps/order/desc.jsp";
	}
	
	//我的订单
	public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("session_user");
		List<Order> orderList = orderService.myOrders(user.getUid());
		request.setAttribute("orderList", orderList);
		
		return "forward:/jsps/order/list.jsp";
	}
	
	//加载订单
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("order", orderService.load(request.getParameter("oid")));
		return "forward:/jsps/order/desc.jsp";
	}
	
	//确认收货
	public String comfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			orderService.comfirm(request.getParameter("oid"));
			request.setAttribute("msg", "确认收货成功！");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "forward:/jsps/msg.jsp";
	}
	
	/**
	 * 支付13+1个参数
	 * 
	https://www.yeepay.com/app-merchant-proxy/node?
	p0_Cmd=Buy&p1_MerId=10001126856
	&p2_Order=7CD0A5A2D3E34CEC9E7CA6D46DE4F16A&p3_Amt=0.01&p4_Cur=CNY
	&p5_Pid=&p6_Pcat=&p7_Pdesc=
	&p8_Url=http://localhost:8080/bookstore/OrderServlet?method=back
	&p9_SAF=&pa_MP=&pd_FrpId=ICBC-NET-B2C&pr_NeedResponse=1
	&hmac=a0983893ab6e647bfb23d7953a89b006
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream("/merchantInfo.properties"));
		
		//13个参数
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = request.getParameter("oid");
		
//		String p3_Amt = request.getParameter("total");
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";
		
		//+1个参数
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid,
				p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId,
				pr_NeedResponse, props.getProperty("keyValue"));
		
		//构建支付网址
		StringBuilder sb = new StringBuilder(props.getProperty("url"));
		sb.append("?p0_Cmd="+p0_Cmd);
		sb.append("&p1_MerId="+p1_MerId);
		sb.append("&p2_Order="+p2_Order);
		sb.append("&p3_Amt="+p3_Amt);
		sb.append("&p4_Cur="+p4_Cur);
		sb.append("&p5_Pid="+p5_Pid);
		sb.append("&p6_Pcat="+p6_Pcat);
		sb.append("&p7_Pdesc="+p7_Pdesc);
		sb.append("&p8_Url="+p8_Url);
		sb.append("&p9_SAF="+p9_SAF);
		sb.append("&pa_MP="+pa_MP);
		sb.append("&pd_FrpId="+pd_FrpId);
		sb.append("&pr_NeedResponse="+pr_NeedResponse);
		sb.append("&hmac="+hmac);
		response.sendRedirect(sb.toString());
		
		return null;
	}
	
	/**
	 * 在线支付完后的回调方法
	 * 11+1个参数
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 11+1个参数
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");

		String hmac = request.getParameter("hmac");
		
		//2. 判断调用者是否为易宝
		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream("/merchantInfo.properties"));
		Boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code,
				r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP,
				r9_BType, props.getProperty("keyValue"));
		if (!bool) {
			request.setAttribute("msg", "非法支付！支付失败！");
			return "forward:/jsps/msg.jsp";
		}
		
		//3. 获取并修改订单状态
		orderService.pay(r6_Order);
		
		//4. 判断回调方法，如果点对点就回馈success开头的字符串
		if (r9_BType.equals("2")) {
			response.getWriter().print("success");
		}
		
		//5. 保存成功信息，转发到msg.jsp
		request.setAttribute("msg", "支付成功，等待发货！");
		return "forward:/jsps/msg.jsp";
	}
}
