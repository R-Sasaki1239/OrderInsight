package orderinsight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orderinsight.model.OrderItem;

public class InMemoryOrderItemRepository implements OrderItemRepository{
	private final Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();
	
	public InMemoryOrderItemRepository() {
        // ▼ テストデータ ▼
        // orderId=1 (userId=3): りんご×2, バナナ×1
        save(new OrderItem(
                0,      // orderItemId（0のままでOK）
                1,      // orderId
                1,      // itemId = productId=1 (りんご)
                2,      // 数量
                120 * 2 // lineTotal
        ));
        save(new OrderItem(
                0,
                1,
                2,      // バナナ
                1,
                80 * 1
        ));

        // orderId=2 (userId=3): ぶどう×1, 白菜×2
        save(new OrderItem(
                0,
                2,
                4,      // ぶどう
                1,
                400 * 1
        ));
        save(new OrderItem(
                0,
                2,
                10,     // 白菜
                2,
                250 * 2
        ));

        // orderId=3 (userId=4): いちご×3
        save(new OrderItem(
                0,
                3,
                3,      // いちご
                3,
                150 * 3
        ));

        // orderId=4 (userId=5): たまご×2, トマト×1
        save(new OrderItem(
                0,
                4,
                9,      // たまご
                2,
                360 * 2
        ));
        save(new OrderItem(
                0,
                4,
                7,      // トマト
                1,
                180 * 1
        ));

        // orderId=5 (userId=5): 羊肉×1, いわし×2
        save(new OrderItem(
                0,
                5,
                12,     // 羊肉
                1,
                400 * 1
        ));
        save(new OrderItem(
                0,
                5,
                8,      // いわし
                2,
                200 * 2
        ));
        // ▲ テストデータここまで ▲
    }
	
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
		int orderId = orderItem.getOrderId();
		List<OrderItem> list = orderItemMap.get(orderId);
		if (list == null) {
			list = new ArrayList<>();
			orderItemMap.put(orderId, list);
		}
		list.add(orderItem);
	}
}
