package orderinsight.model.report;

//売り上げの統計の準備?
public class ProductSalesStats {
	private final int productId;
	private final String name;
	private final int price;
	private final int soldCount;
	private final int salesAmount;
	private final double salesRatio;

	public ProductSalesStats(int productId, String name, int price, int soldCount, int salesAmount, double salesRatio) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.soldCount = soldCount;
		this.salesAmount = salesAmount;
		this.salesRatio = salesRatio;
	}

	public int getProductId() { 
    	return productId; 
    	}
    
	public String getName() { 
    	return name;
    	}
    
	public int getPrice() { 
    	return price;
    	}
    
	public int getSoldCount() { 
    	return soldCount;
    	}
    
	public int getSalesAmount() { 
    	return salesAmount;
    	}
    
	public double getSalesRatio() {
    	return salesRatio;
    	}
}