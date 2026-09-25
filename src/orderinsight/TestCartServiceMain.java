// TestCartServiceMain.java
package orderinsight;

import orderinsight.model.Cart;
import orderinsight.model.CartItem;
import orderinsight.model.User;
import orderinsight.service.CartService;
import orderinsight.util.InMemoryProductRepository;
import orderinsight.util.InMemoryUserRepository;
import orderinsight.util.ProductRepository;
import orderinsight.util.UserRepository;

public class TestCartServiceMain {

    public static void main(String[] args) {

        // ① Repository 準備
        UserRepository userRepo = new InMemoryUserRepository();
        ProductRepository productRepo = new InMemoryProductRepository();

        // ② Service 準備
        CartService cartService = new CartService(productRepo);

        // ③ ユーザー取得（InMemoryUserRepository の admin）
        User user = userRepo.findByEmail("admin@example.com");

        // ④ カートに追加（正常パターン）
        System.out.println("=== カートに追加（りんご 2個, バナナ 1個） ===");
        boolean add1 = cartService.addToCart(user, 1, 2); // productId=1 りんご
        boolean add2 = cartService.addToCart(user, 2, 1); // productId=2 バナナ
        System.out.println("add1 result = " + add1);
        System.out.println("add2 result = " + add2);

        printCart(cartService.getCart(user));

        // ⑤ 在庫以上を入れようとしたとき（異常パターン）
        System.out.println("\n=== 在庫以上をカートに入れようとするテスト ===");
        boolean addTooMany = cartService.addToCart(user, 1, 1000); // さすがに在庫超え
        System.out.println("addTooMany result = " + addTooMany);
        printCart(cartService.getCart(user)); // 中身が変わっていないことを確認

        // ⑥ 特定商品をカートから削除（clearCart の動き）
        System.out.println("\n=== りんご(ID=1) をカートから削除 ===");
        cartService.removeFromCart(user, 1); 
        printCart(cartService.getCart(user));
    }

    private static void printCart(Cart cart) {
        System.out.println("--- カート内容 (userId=" + cart.getUserId() + ") ---");
        if (cart.getItems().isEmpty()) {
            System.out.println("カートは空です。");
            return;
        }
        for (CartItem ci : cart.getItems()) {
            System.out.printf("productId:%d, quantity:%d%n",
                    ci.getProductId(), ci.getQuantity());
        }
    }
}