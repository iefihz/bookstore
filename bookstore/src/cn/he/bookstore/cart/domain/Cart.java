package cn.he.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车
 * @author He
 *
 */
public class Cart {
	
	//以bid为key，CartItem为value
	private Map<String, CartItem> map = new LinkedHashMap<String, CartItem>();

	//总计
	public double getTotal() {
		BigDecimal total = new BigDecimal("0");
		for (CartItem cartItem : map.values()) {
			BigDecimal subtotal = new BigDecimal(
					Double.toString(cartItem.getSubtotal()));
			total = total.add(subtotal);
		}
		return total.doubleValue();
	}
	
	//添加条目
	public void add(CartItem cartItem) {
		if (map.containsKey(cartItem.getBook().getBid())) {
			CartItem old_cartItem = map.get(cartItem.getBook().getBid());
			old_cartItem.setCount(old_cartItem.getCount()+cartItem.getCount());
			map.put(old_cartItem.getBook().getBid(), old_cartItem);
		} else {
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	
	//清空购物车
	public void clear() {
		map.clear();
	}
	
	//移除条目
	public void remove(String bid) {
		map.remove(bid);
	}
	
	//我的购物车
	public Collection<CartItem> getCartItems() {
		return map.values();
	}
}
