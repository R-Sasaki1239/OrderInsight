package orderinsight;

import orderinsight.model.Order;
import orderinsight.util.InMemoryOrderRepository;
import orderinsight.util.OrderRepository;

public class TestOrderRepositoryMain {
    public static void main(String[] args) {
        OrderRepository repo = new InMemoryOrderRepository();

        Order o = new Order(1, 1, "神奈川県横浜市", 3000); 
        repo.save(o);

        System.out.println("保存された orderId = " + o.getOrderId());

        Order found = repo.findById(o.getOrderId());
        System.out.println(found != null ? "findById OK" : "findById NG");

        System.out.println("全件数 = " + repo.findAll().size());
    }
}