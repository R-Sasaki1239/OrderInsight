package orderinsight.model;

import java.time.LocalDateTime;

public class Order {
	private int orderId;
	private int orderUserId;
	private int paymentMethod;
	private String shippingAddress;
	private int totalAmount;
	private LocalDateTime createDateTime;
	
	public Order() {
	}

	public Order(int orderUserId, int paymentMethod, String shippingAddress, int totalAmount) {
		this.orderUserId = orderUserId;
		this.paymentMethod = paymentMethod;
		this.shippingAddress = shippingAddress;
		this.totalAmount = totalAmount;
		this.createDateTime = LocalDateTime.now();
	}

	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getOrderUserId() {
		return orderUserId;
	}

	public int getPaymentMethod() {
		return paymentMethod;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}
}
