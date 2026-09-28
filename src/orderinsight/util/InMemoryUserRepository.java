package orderinsight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orderinsight.model.User;

public class InMemoryUserRepository implements UserRepository{

	private final Map<Integer, User> userMap = new HashMap<Integer, User>();
	
	public InMemoryUserRepository() {
		User admin1 = new User(
			1,
			"admin",
			"admin",
			"password",
			"神奈川県横浜市",
			"adminemail",
			0,
			"ADMIN"
		);
		User admin2 = new User(
			2,
			"あ",
			"あ",
			"あ",
			"あ",
			"あ",
			0,
			"ADMIN"
		);
		User user1 = new User(
			3,
			"佐々木瞭",
			"",
			"password",
			"日本",
			"useremail",
			0,
			"USER"
		);
		User user2 = new User(
			4,
			"あ",
			"あ",
			"あ",
			"あ",
			"あ",
			0,
			"USER"
		);
		User user3 = new User(
			5,
			"鈴木",
			"鈴木",
			"suzuki",
			"日本",
			"suzuki@email",
			0,
			"USER"
		);
		
		save(admin1);
		save(admin2);
		save(user1);
		save(user2);
		save(user3);
	}
	
	//userIdで指定して情報見る
	@Override
	public User findById(int userId) {
		return userMap.get(userId);
	}
	
//	ログインでemailでpasswordの確認用
//	Userの方で全ての情報あるいはnullを返す
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
