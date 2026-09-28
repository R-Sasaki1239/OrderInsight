package orderinsight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orderinsight.model.Order;

public class InMemoryOrderRepository implements OrderRepository{
	
	private final Map<Integer, Order> orderMap = new HashMap<>();
	private int nextId = 1;
	
	public InMemoryOrderRepository() {
        // ▼ テストデータ ▼
        // userId=3（佐々木瞭）が2回注文
        Order o1 = new Order(
                3,          // orderUserId
                1,          // paymentMethod（1:クレジット）
                "日本",      // shippingAddress
                120*2 + 80      // totalAmount (りんご2 + バナナ1)
        );
        save(o1); // orderId=1 になる想定

        Order o2 = new Order(
                3,
                2,          // paymentMethod（2:銀行振込）
                "日本",
                400*1 + 250*2  // ぶどう1 + 白菜2
        );
        save(o2); // orderId=2

        // userId=4（あ）が1回注文（いちご ×3）
        Order o3 = new Order(
                4,
                1,
                "あ",
                150*3
        );
        save(o3); // orderId=3

        // userId=5（鈴木）が2回注文
        Order o4 = new Order(
                5,
                1,
                "日本",
                360*2 + 180*1   // たまご2 + トマト1
        );
        save(o4); // orderId=4

        Order o5 = new Order(
                5,
                2,
                "日本",
                400*1 + 200*2   // 羊肉1 + いわし2
        );
        save(o5); // orderId=5
        // ▲ テストデータここまで ▲
    }
	
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
	
	@Override
	public List<Order> findByUserId(int userId){
		List<Order> personOrders = new ArrayList<>();
		for (Order order : orderMap.values()) {
			if (order.getOrderUserId() == userId) {
				personOrders.add(order);
			}
		}
		return personOrders;
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