package cn.he.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.he.bookstore.order.dao.OrderDao;
import cn.he.bookstore.order.domain.Order;
import cn.he.utils.JdbcUtils;

public class OrderService {

	private OrderDao orderDao = new OrderDao();
	
	//添加订单
	public void add(Order order) {
		try {
			JdbcUtils.beginTransaction();
			
			//添加订单以及添加订单中的条目，是同一个事务
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException(e);
		}
	}

	//我的订单
	public List<Order> myOrders(String uid) {
		return orderDao.findByUid(uid);
	}

	//加载订单
	public Order load(String oid) {
		return orderDao.load(oid);
	}

	//确认收货
	public void comfirm(String oid) throws OrderException {
		int state = orderDao.getStateByOid(oid);
		if (3 != state) {
			throw new OrderException("确认收货失败！");
		}
		orderDao.modifyState(oid, 4);
	}

	//付款
	public void pay(String oid) {
		int state = orderDao.getStateByOid(oid);
		
		//订单状态为1时，改状态为2
		if (1 == state) {
			orderDao.modifyState(oid, 2);
		}
	}

	//所有订单
	public List<Order> allOrders() {
		return orderDao.allOrders();
	}

	//按订单状态查找订单
	public List<Order> getOrdersByState(String state) {
		return orderDao.getOrderByState(state);
	}

	//修改订单状态
	public void changeState(String oid, int state) {
		orderDao.changeState(oid, state);
	}
}
