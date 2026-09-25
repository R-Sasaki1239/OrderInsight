package orderinsight.model.report;

//商品の統計準備
public class ProductStats {
	private final int productId;
	private final String name;
	private final int totalCount;
	private final int stock;
	private final int soldCount;
	private final double soldRatio;
	private final boolean active;
	private final String createdAt;

	public ProductStats(int productId, String name, int totalCount, int stock, int soldCount, double soldRatio,
			boolean active, String createdAt) {
		this.productId = productId;
		this.name = name;
		this.totalCount = totalCount;
		this.stock = stock;
		this.soldCount = soldCount;
		this.soldRatio = soldRatio;
		this.active = active;
		this.createdAt = createdAt;
	}

	public int getProductId() { 
		return productId;
		}
	
	public String getName() {
		return name;
		}
	
	public int getTotalCount() {
		return totalCount;
		}
	
	public int getStock() {
		return stock;
		}
	
	public int getSoldCount() {
		return soldCount;
		}
	
	public double getSoldRatio() {
		return soldRatio;
		}
	
	public boolean isActive() {
		return active;
		}
	
	public String getCreatedAt() {
		return createdAt;
		}
}