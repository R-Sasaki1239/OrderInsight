package orderinsight.util;

import java.util.List;

import orderinsight.model.Order;

public interface OrderRepository {
	Order findById(int orderId);
	List<Order> findAll();
	void save(Order order);
}