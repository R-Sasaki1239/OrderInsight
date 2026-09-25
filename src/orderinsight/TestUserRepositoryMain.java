package orderinsight;

import orderinsight.model.User;
import orderinsight.util.InMemoryUserRepository;
import orderinsight.util.UserRepository;

public class TestUserRepositoryMain {
    public static void main(String[] args) {
        UserRepository repo = new InMemoryUserRepository();

        System.out.println("=== User 全件 ===");
        for (User u : repo.findAll()) {
            System.out.printf("ID:%d, name:%s, email:%s, role:%s%n",
                    u.getUserId(), u.getName(), u.getEmail(), u.getRole());
        }

        System.out.println("\n=== email で取得 ===");
        User admin = repo.findByEmail("admin@example.com");
        System.out.println(admin != null ? "見つかった: " + admin.getName() : "見つからない");
    }
}