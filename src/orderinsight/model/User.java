package orderinsight.model;

import java.time.LocalDateTime;

public class User {
	private int userId;
	private String name;
	private String nickname;
	private String password;
	private String address;
	private String email;
	private int totalSpentAmount;
	private String role;
	private LocalDateTime createdAt; 
	
	public User() {
    }
	
	public User(int userId, String name, String nickname, String password, String address, String email,
			int totalSpentAmount, String role) {
		this.userId = userId;
		this.name = name;
		this.nickname = nickname;
		this.password = password;
		this.address = address;
		this.email = email;
		this.totalSpentAmount = totalSpentAmount;
		this.role = role;
		this.createdAt = LocalDateTime.now();
	}

	public int getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTotalSpentAmount() {
		return totalSpentAmount;
	}
	
	public void setTotalSpentAmount(int totalSpentAmount) {
	    this.totalSpentAmount = totalSpentAmount;
	}

	public void addToTotalSpentAmount(int amount) {
	    this.totalSpentAmount += amount;
	}

	public String getRole() {
		return role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}