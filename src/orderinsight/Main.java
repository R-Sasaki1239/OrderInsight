package orderinsight;

import orderinsight.controller.Menu;
import orderinsight.service.AuthService;
import orderinsight.service.CartService;
import orderinsight.service.OrderService;
import orderinsight.service.ProductService;
import orderinsight.service.ReportService;
import orderinsight.util.InMemoryOrderItemRepository;
import orderinsight.util.InMemoryOrderRepository;
import orderinsight.util.InMemoryProductRepository;
import orderinsight.util.InMemoryUserRepository;
import orderinsight.util.OrderItemRepository;
import orderinsight.util.OrderRepository;
import orderinsight.util.ProductRepository;
import orderinsight.util.UserRepository;

public class Main {
	public static void main(String[] args) {
		UserRepository userRepository = new InMemoryUserRepository();
		ProductRepository productRepository = new InMemoryProductRepository();
		OrderRepository orderRepository = new InMemoryOrderRepository();
		OrderItemRepository orderItemRepository = new InMemoryOrderItemRepository();
		
		AuthService authService = new AuthService(userRepository);
		ProductService productService = new ProductService(productRepository);
		CartService cartService = new CartService(productRepository);
		OrderService orderService = new OrderService(orderRepository, orderItemRepository, productRepository);
		ReportService reportService = new ReportService(orderRepository, orderItemRepository, productRepository,userRepository);
		
		Menu menu = new Menu(authService, productService, cartService, orderService, reportService);
		menu.start();
	}
}