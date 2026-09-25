package orderinsight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orderinsight.model.Order;

public class InMemoryOrderRepository implements OrderRepository{
	
	private final Map<Integer, Order> orderMap = new HashMap<>();
	private int nextId = 1;
	
	//orderIdで指定して情報を見る
	@Override
	public Order findById(int orderId) {
		return orderMap.get(orderId);
	}
	
	//order一覧
	@Override
	public List<Order> findAll(){
		return new ArrayList<>(orderMap.values());
	}
	
	//orderの保存
	@Override
	public void save(Order order) {
		if (order.getOrderId() == 0) {
			order.setOrderId(nextId++);
		}
		orderMap.put(order.getOrderId(), order);
	}
}