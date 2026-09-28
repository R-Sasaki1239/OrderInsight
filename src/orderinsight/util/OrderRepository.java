package orderinsight.util;

import java.util.List;

import orderinsight.model.Order;

public interface OrderRepository {
	Order findById(int orderId);
	List<Order> findAll();
	List<Order> findByUserId(int userId);
	void save(Order order);
}