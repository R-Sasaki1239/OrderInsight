package orderinsight;

import java.util.List;

import orderinsight.model.OrderItem;
import orderinsight.util.InMemoryOrderItemRepository;
import orderinsight.util.OrderItemRepository;

public class TestOrderItemRepositoryMain {
    public static void main(String[] args) {
        OrderItemRepository repo = new InMemoryOrderItemRepository();

        OrderItem item1 = new OrderItem(0, 1, 1, 2, 240); // orderId=1
        OrderItem item2 = new OrderItem(0, 1, 2, 1, 80);  // orderId=1
        repo.save(item1);
        repo.save(item2);

        List<OrderItem> list = repo.findByOrderId(1);
        System.out.println("orderId=1 の明細数 = " + list.size());

        System.out.println("全明細数 = " + repo.findAll().size());
    }
}