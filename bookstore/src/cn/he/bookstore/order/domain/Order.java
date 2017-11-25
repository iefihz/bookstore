package cn.he.bookstore.order.domain;

import java.util.Date;
import java.util.List;

import cn.he.bookstore.user.domain.User;

public class Order {
	private String oid;
	
	private Date ordertime;
	
	private double total;
	
	//1:未付款 	2:付款未发货	3:发货未确认收货	4:确认收货
	private int state;
	
	private User owner;
	
	private String address;
	
	//订单的所有订单条目
	private List<OrderItem> orderItemList;
	
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemLists(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
