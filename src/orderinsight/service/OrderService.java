package orderinsight.service;

import java.util.List;

import orderinsight.model.Cart;
import orderinsight.model.CartItem;
import orderinsight.model.Order;
import orderinsight.model.OrderItem;
import orderinsight.model.Product;
import orderinsight.model.User;
import orderinsight.util.OrderItemRepository;
import orderinsight.util.OrderRepository;
import orderinsight.util.ProductRepository;

public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	
	public OrderService(OrderRepository orderRepository,
			OrderItemRepository orderItemRepository,
			ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
	}
	
	public Order createOrderFromCart(User user,Cart cart,int paymentMethod,String shippingAddress) {
		List<CartItem> items = cart.getItems();
		if (items.isEmpty()) {
			return null; 
		}
		
		//合計金額を計算
		int totalAmount = 0;
		for (CartItem ci : items) {
			Product p = productRepository.findById(ci.getProductId());
			if (p == null || !p.isActive()) {
				continue;
			}
			int lineTotal = p.getProductPrice() * ci.getQuantity();
			totalAmount += lineTotal;
		}
		
		//Order を作成
		Order order = new Order(user.getUserId(),paymentMethod,shippingAddress,totalAmount);
		
		orderRepository.save(order); 
		
		int orderId = order.getOrderId();
		
		//OrderItemを作成 ＋ 在庫を更新
		for (CartItem ci : items) {
			Product p = productRepository.findById(ci.getProductId());
			if (p == null || !p.isActive()) {
				continue;
			}
		
			int quantity = ci.getQuantity();
			int lineTotal = p.getProductPrice() * quantity;
		
			OrderItem orderItem = new OrderItem(0, orderId, ci.getProductId(), quantity, lineTotal);
			orderItemRepository.save(orderItem);
		
			//在庫を減らす
			int newStock = p.getProductStock() - quantity;
			p.setProductStock(newStock);
			productRepository.save(p);
		}
		
		//カートを空にする
		cart.clear();
		
		return order;
	}  
}