package orderinsight;

import orderinsight.service.AuthService;
import orderinsight.util.InMemoryUserRepository;
import orderinsight.util.UserRepository;

public class TestAuthServiceMain {
    public static void main(String[] args) {
        UserRepository userRepo = new InMemoryUserRepository();
        AuthService authService = new AuthService(userRepo);

        //adminでログインテスト
        boolean ok1 = authService.login("admin@example.com", "sasaki0818");
        System.out.println("admin ログイン結果: " + ok1);
        System.out.println("isAdmin = " + authService.isAdmin());

        //userでログインテスト
        boolean ok2 = authService.login("taro@example.com", "pass123");
        System.out.println("taro ログイン結果: " + ok2);
        System.out.println("isAdmin = " + authService.isAdmin());
    }
}
