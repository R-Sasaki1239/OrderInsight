package orderinsight.util;

import java.util.List;

import orderinsight.model.OrderItem;

public interface OrderItemRepository {
	List<OrderItem> findByOrderId(int orderId);
	List<OrderItem> findAll();
	void save(OrderItem orderItem);
}
