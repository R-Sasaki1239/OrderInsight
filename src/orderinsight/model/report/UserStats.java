package orderinsight.model.report;

//顧客統計の準備
public class UserStats {
	private final int userId;
	private final String name;
	private final String address;
	private final String email;
	private final int totalSpent;
	private final int orderCount;
	private final double salesRatio;
	private final String role;
	private final String createdAt;

	public UserStats(int userId, String name, String address, String email, int totalSpent, int orderCount,
			double salesRatio, String role, String createdAt) {
		super();
		this.userId = userId;
		this.name = name;
		this.address = address;
		this.email = email;
		this.totalSpent = totalSpent;
		this.orderCount = orderCount;
		this.salesRatio = salesRatio;
		this.role = role;
		this.createdAt = createdAt;
	}

	public int getUserId() {
		return userId;
		}
	
	public String getName() {
		return name;
		}
	
	public String getAddress() {
		return address;
		}
	
	public String getEmail() {
		return email;
		}
	
	public int getTotalSpent() {
		return totalSpent;
		}
	
	public int getOrderCount() {
		return orderCount;
		}
	
	public double getSalesRatio() {
		return salesRatio;
		}
	
	public String getRole() {
		return role;
		}
	
	public String getCreatedAt() {
		return createdAt;
		}
}