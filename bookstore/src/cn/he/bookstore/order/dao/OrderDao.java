package cn.he.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.he.bookstore.book.domain.Book;
import cn.he.bookstore.order.domain.Order;
import cn.he.bookstore.order.domain.OrderItem;
import cn.he.utils.CommonUtils;
import cn.he.utils.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();

	//添加订单
	public void addOrder(Order order) {
		String sql = "INSERT INTO orders VALUES(?, ?, ?, ?, ?, ?)";
		Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
		Object[] params = {order.getOid(), timestamp, order.getTotal(),
				order.getState(), order.getOwner().getUid(), order.getAddress()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//添加订单里面的条目，由于订单的条目有多个，要批处理
	public void addOrderItemList(List<OrderItem> orderItemList) {
		String sql = "INSERT INTO orderitem VALUES (?, ?, ?, ?, ?)";
		Object[][] params = new Object[orderItemList.size()][];
		
		/*
		 * 给params赋值
		 * orderItemList里每一个OrderItem就是一个二维数组里面的一个元素(一位数组)
		 */
		for (int i = 0; i < orderItemList.size(); i++) {
			OrderItem orderItem = orderItemList.get(i);
			params[i] = new Object[]{orderItem.getIid(), orderItem.getCount(),
					orderItem.getSubtotal(), orderItem.getOrder().getOid(),
					orderItem.getBook().getBid()};
		}
		try {
			qr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//按uid查询订单
	public List<Order> findByUid(String uid) {
		String sql = "SELECT * FROM orders WHERE uid=? ORDER BY ordertime DESC";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 为每一个订单加载所有订单条目，
	 * 由于查询出来的东西不会自动映射到OrderItem对象，所有要用MapListHandler
	 * @param order
	 */
	private void loadOrderItems(Order order) {
		String sql = "SELECT * FROM orderitem oi INNER JOIN book b ON oi.bid=b.bid WHERE oid=?";
		try {
			List<Map<String, Object>> mapList = qr.query(sql,
					new MapListHandler(), order.getOid());
			
			List<OrderItem> orderItemList = toOrderItemList(mapList);
			
			order.setOrderItemLists(orderItemList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	//把mapList转化为orderItemList
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (Map<String, Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}

	/**
	 * 通过map生成对应的OrderItem以及Book对象，把Book对象添加到条目中，返回条目
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.getBeanFromMap(map, OrderItem.class);
		Book book = CommonUtils.getBeanFromMap(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	public Order load(String oid) {
		String sql = "SELECT * FROM orders WHERE oid=?";
		try {
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
			
			loadOrderItems(order);
			
			return order;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	//根据oid获取订单状态
	public int getStateByOid(String oid) {
		String sql = "SELECT state FROM orders WHERE oid=?";
		try {
			return qr.query(sql, new ScalarHandler<Integer>(), oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//修改订单状态
	public void modifyState(String oid, int state) {
		String sql = "UPDATE orders SET state=? WHERE oid=?";
		try {
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//加载所有订单
	public List<Order> allOrders() {
		String sql = "SELECT * FROM orders ORDER BY ordertime DESC";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class));
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//按订单状态加载订单
	public List<Order> getOrderByState(String state) {
		String sql = "SELECT * FROM orders WHERE state=? ORDER BY ordertime DESC";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), state);
			for (Order order : orderList) {
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//改变订单状态
	public void changeState(String oid, int state) {
		String sql = "UPDATE orders SET state=? WHERE oid=?";
		try {
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
