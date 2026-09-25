package orderinsight.service;

import orderinsight.model.User;
import orderinsight.util.UserRepository;

public class AuthService {
	private final UserRepository userRepository;
	private User loggedInUser;
	
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	//	ログイン処理
	public boolean login(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return false;
		}
		if (!user.getPassword().equals(password)) {
			return false;
		}
		this.loggedInUser = user;
		return true;
	}
	
	//	ログアウト処理
	public void logout() {
		this.loggedInUser = null;
	}
	
	//	登録処理
	public User register(String name, String nickname, String password, String address, String email){
		int newId = userRepository.findAll().size() + 1;
		
		User user = new User(newId, name, nickname, password, address, email, 0, "USER");
		
		userRepository.save(user);
		return user;
	}
	
	//	ログイン中のユーザーを返す
	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	//	ロールの確認
	public boolean isAdmin() {
		return loggedInUser != null && "ADMIN".equals(loggedInUser.getRole()); 
	}
	
	public boolean isLoggedIn() {
		return loggedInUser != null;
	}
	
}
