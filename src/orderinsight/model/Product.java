package orderinsight.model;

import java.time.LocalDateTime;

public class Product {
	private int productId;
	private String productName;
	private int productPrice;
	private int productStock;
	private boolean active;
	private LocalDateTime createdAt;
	
	public Product(){
	}

	public Product(int productId, String productName, int productPrice, int productStock, boolean active) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productStock = productStock;
		this.active = active;
		this.createdAt = LocalDateTime.now();
	}

	public int getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}
	
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public int getProductStock() {
		return productStock;
	}
	
	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
