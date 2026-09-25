package orderinsight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orderinsight.model.User;

public class InMemoryUserRepository implements UserRepository{

	private final Map<Integer, User> userMap = new HashMap<Integer, User>();
	
	public InMemoryUserRepository() {
		User admin = new User(
			1,
			"admin",
			"管理者",
			"sasaki0818",
			"神奈川県横浜市",
			"admin@example.com",
			0,
			"ADMIN"
		);
		User user = new User(
			2,
			"山田太郎",
			"taro",
			"pass123",
			"東京都新宿区",
			"taro@example.com",
			0,
			"USER"
		);
		save(admin);
		save(user);
	}
	
	//userIdで指定して情報見る
	@Override
	public User findById(int userId) {
		return userMap.get(userId);
	}
	
//	ログインでemailでpasswordの確認用
	@Override
	public User findByEmail(String email) {
		for (User user : userMap.values()) {
			if (user.getEmail().equals(email)) {
				return user;
			}
		}
		return null;
	}
	
//	user一覧
	@Override
	public List<User> findAll(){
		return new ArrayList<>(userMap.values());
	}
	
	@Override
	public void save(User user) {
		userMap.put(user.getUserId(), user);
	}
}
