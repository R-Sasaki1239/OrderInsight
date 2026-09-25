package orderinsight.util;

import java.util.List;

import orderinsight.model.User;

public interface UserRepository {
	User findById(int userId);
	User findByEmail(String email);
	List<User> findAll();
	void save(User user);
}