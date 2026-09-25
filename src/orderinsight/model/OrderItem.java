package orderinsight.model;

public class OrderItem {
	private int orderItemId;
	private int orderId;
	private int itemId;
	private int itemQuantity;
	private int lineTotal;
	
	public OrderItem() {
	}

	public OrderItem(int orderItemId, int orderId, int itemId, int itemQuantity, int lineTotal) {
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.itemId = itemId;
		this.itemQuantity = itemQuantity;
		this.lineTotal = lineTotal;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public int getLineTotal() {
		return lineTotal;
	}
}
