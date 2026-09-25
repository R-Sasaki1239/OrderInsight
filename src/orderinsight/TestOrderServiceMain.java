// TestOrderServiceMain.java
package orderinsight;

import java.util.List;

import orderinsight.model.Cart;
import orderinsight.model.CartItem;
import orderinsight.model.Order;
import orderinsight.model.OrderItem;
import orderinsight.model.Product;
import orderinsight.model.User;
import orderinsight.service.OrderService;
import orderinsight.util.InMemoryOrderItemRepository;
import orderinsight.util.InMemoryOrderRepository;
import orderinsight.util.InMemoryProductRepository;
import orderinsight.util.InMemoryUserRepository;
import orderinsight.util.OrderItemRepository;
import orderinsight.util.OrderRepository;
import orderinsight.util.ProductRepository;
import orderinsight.util.UserRepository;

public class TestOrderServiceMain {

    public static void main(String[] args) {

        // ① Repository 準備
        UserRepository userRepo = new InMemoryUserRepository();
        ProductRepository productRepo = new InMemoryProductRepository();
        OrderRepository orderRepo = new InMemoryOrderRepository();
        OrderItemRepository orderItemRepo = new InMemoryOrderItemRepository();

        // ② Service 準備
        OrderService orderService = new OrderService(orderRepo, orderItemRepo, productRepo);

        // ③ ユーザーとカートを用意
        User admin = userRepo.findByEmail("admin@example.com"); // InMemoryUserRepository の初期ユーザー
        Cart cart = new Cart(admin.getUserId());

        // カートに商品を追加（例: りんご2個, バナナ1個）
        cart.addItem(1, 2); // productId=1 りんご, quantity=2
        cart.addItem(2, 1); // productId=2 バナナ, quantity=1

        // ④ 注文確定
        int paymentMethod = 1;                 // 仮の支払い方法
        String shippingAddress = admin.getAddress();

        Order order = orderService.createOrderFromCart(admin, cart, paymentMethod, shippingAddress);

        if (order == null) {
            System.out.println("注文が作成されませんでした（カートが空？）");
            return;
        }

        System.out.println("=== 注文確定結果 ===");
        System.out.println("orderId = " + order.getOrderId());
        System.out.println("userId  = " + order.getOrderUserId());
        System.out.println("totalAmount = " + order.getTotalAmount());
        System.out.println("shippingAddress = " + order.getShippingAddress());
        System.out.println("createDateTime  = " + order.getCreateDateTime());

        // ⑤ OrderItem の確認
        System.out.println("\n=== OrderItem 一覧 (orderId=" + order.getOrderId() + ") ===");
        List<OrderItem> orderItems = orderItemRepo.findByOrderId(order.getOrderId());
        for (OrderItem oi : orderItems) {
            System.out.printf(
                    "orderItemId:%d | orderId:%d | productId:%d | quantity:%d | lineTotal:%d%n",
                    oi.getOrderItemId(),
                    oi.getOrderId(),
                    oi.getItemId(),
                    oi.getItemQuantity(),
                    oi.getLineTotal()
            );
        }

        // ⑥ 在庫が減っているか確認
        System.out.println("\n=== 在庫の確認 ===");
        Product p1 = productRepo.findById(1);
        Product p2 = productRepo.findById(2);
        System.out.printf("りんご(ID=1) の在庫: %d%n", p1.getProductStock());
        System.out.printf("バナナ(ID=2) の在庫: %d%n", p2.getProductStock());

        // ⑦ カートが空になっているか確認
        System.out.println("\n=== カートの中身 ===");
        if (cart.getItems().isEmpty()) {
            System.out.println("カートは空です。");
        } else {
            System.out.println("カートにまだ残っています。");
            for (CartItem ci : cart.getItems()) {
                System.out.printf("productId:%d, quantity:%d%n", ci.getProductId(), ci.getQuantity());
            }
        }
    }
}