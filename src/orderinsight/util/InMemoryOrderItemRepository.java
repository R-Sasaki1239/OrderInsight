package orderinsight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orderinsight.model.OrderItem;

public class InMemoryOrderItemRepository implements OrderItemRepository{
	private final Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();
	
	@Override
	public List<OrderItem> findByOrderId(int orderId){
		List<OrderItem> list = orderItemMap.get(orderId);
		if (list == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(list);
	}
	
	@Override
	public List<OrderItem> findAll(){
		List<OrderItem> all = new ArrayList<>();
		for (List<OrderItem>list : orderItemMap.values()) {
			all.addAll(list);
		}
		return all;
	}
	
	@Override
	public void save(OrderItem orderItem) {
		int orderId = orderItem.getItemId();
		List<OrderItem> list = orderItemMap.get(orderId);
		if (list == null) {
			list = new ArrayList<>();
			orderItemMap.put(orderId, list);
		}
		list.add(orderItem);
	}
}
