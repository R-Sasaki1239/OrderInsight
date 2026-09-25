package orderinsight.controller;

import java.util.List;
import java.util.Scanner;

import orderinsight.model.Cart;
import orderinsight.model.CartItem;
import orderinsight.model.Order;
import orderinsight.model.Product;
import orderinsight.model.User;
import orderinsight.model.report.ProductSalesStats;
import orderinsight.model.report.ProductStats;
import orderinsight.model.report.UserStats;
import orderinsight.service.AuthService;
import orderinsight.service.CartService;
import orderinsight.service.OrderService;
import orderinsight.service.ProductService;
import orderinsight.service.ReportService;

public class Menu {
	private final AuthService authService;
	private final ProductService productService;
	private final CartService cartService;
	private final OrderService orderService;
	private final ReportService reportService;
	
	private final Scanner scanner = new Scanner(System.in);

	public Menu(AuthService authService,
				ProductService productService,
				CartService cartService,
				OrderService orderService,
				ReportService reportService) {
			this.authService = authService;
			this.productService = productService;
			this.cartService = cartService;
			this.orderService = orderService;
			this.reportService = reportService;
	}

	//起動時共通メニュー(仮)
	public void start() {
		while (true) {
			System.out.println("=== OrderInsight ===");
			System.out.println("1. ログイン");
			System.out.println("2. 新規登録");
			System.out.println("3. 終了");
			System.out.print("番号を選んでください: ");
			
			String input = scanner.nextLine();
			
			switch (input) {
			case "1":
				handleLogin();
				break;
			case "2":
				handleRegister();
				break;
			case "3":
				System.out.println("終了します。");
				return;
			default:
			    System.out.println("不正な入力です。");
			}
		}
	}
//	ログイン
	public void handleLogin() {
		System.out.print("メールアドレス: ");
		String email = scanner.nextLine();
		System.out.print("パスワード: ");
		String password = scanner.nextLine();
	
		boolean match = authService.login(email, password);
		
		if (!match) {
			System.out.println("メールアドレスまたはパスワードが違います。");
			return;
		}
		
		User user = authService.getLoggedInUser();
		System.out.println("ようこそ " + user.getNickname() + " さん");
		
		if (authService.isAdmin()) {
			showAdminMenu();
		} else {
			showUserMenu();
		}
	}
	
	//新規登録
	public void handleRegister() {
		System.out.print("名前: ");
		String name = scanner.nextLine();
		System.out.print("ニックネーム: ");
		String nickname = scanner.nextLine();
		System.out.print("メールアドレス: ");
		String email = scanner.nextLine();
		
		System.out.print("パスワード: ");
		String password1 = scanner.nextLine();
		System.out.print("パスワード（確認用）: ");
		String password2 = scanner.nextLine();
		if (!password1.equals(password2)) {
		    System.out.println("パスワードが一致しません。もう一度やり直してください。");
		    return;
		}
		
		System.out.print("住所: ");
		String address = scanner.nextLine();
		
		User user = authService.register(name, nickname, password1, address, email);
		System.out.println("登録が完了しました。ようこそ " + user.getNickname() + " さん");
	
		authService.login(email, password1);
		if (authService.isAdmin()) {
			showAdminMenu();
		} else {
			showUserMenu();
		}
	}

    //管理者メニュー
	//＊つまらない、動くよう
    private void showAdminMenu() {
        while (true) {
            System.out.println("\n=== 管理者メニュー ===");
            System.out.println("1. 全商品一覧を表示");
            System.out.println("2. 販売中の商品一覧を表示");
            System.out.println("3. 販売停止中の商品一覧を表示");
            System.out.println("4. 商品を新規登録");
            System.out.println("5. 商品情報を更新");
            System.out.println("6. 商品を販売終了（論理削除）");
            System.out.println("7. レポートメニュー");
            System.out.println("8. ログアウトしてメインメニューに戻る");
            System.out.print("番号を選んでください: ");

            String input = scanner.nextLine();

            switch (input) {
            case "1":
                showProducts(productService.getAllProducts(), "全商品");
                break;
            case "2":
                showProducts(productService.getActiveProducts(), "販売中の商品");
                break;
            case "3":
                showProducts(productService.getInactiveProducts(), "販売停止中の商品");
                break;
            case "4":
                handleCreateProduct();
                break;
            case "5":
                handleUpdateProduct();
                break;
            case "6":
                handleDeactivateProduct();
                break;
            case "7":
                showReportMenu();
                break;
            case "8":
                System.out.println("ログアウトします。");
                authService.logout();
                return;
            default:
                System.out.println("不正な入力です。");
            }
        }
    }

    //商品一覧表示(userもできる)
    private void showProducts(List<Product> products, String title) {
        System.out.println("\n=== " + title + " ===");
        if (products.isEmpty()) {
            System.out.println("商品がありません。");
            return;
        }
        for (Product product : products) {
            System.out.printf(
                    "ID:%d | 名前:%s | 価格:%d | 在庫:%d | active:%b%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getProductStock(),
                    product.isActive());
        }
    }

    // 商品新規登録
    private void handleCreateProduct() {
        System.out.print("商品名: ");
        String name = scanner.nextLine();
        System.out.print("価格: ");
        int price = Integer.parseInt(scanner.nextLine());
        System.out.print("在庫数: ");
        int stock = Integer.parseInt(scanner.nextLine());
        System.out.print("販売中にしますか？ (1:はい / 0:いいえ): ");
        boolean active = "1".equals(scanner.nextLine());

        Product product = productService.createProduct(name, price, stock, active);
        System.out.println("商品を登録しました。ID = " + product.getProductId());
    }

    // 商品更新
    private void handleUpdateProduct() {
        System.out.print("更新したい商品のID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Product product = productService.getProductById(id);
        if (product == null) {
            System.out.println("該当する商品がありません。");
            return;
        }

        System.out.println("現在の情報: " + product.getProductName() + " / 価格:" + product.getProductPrice() + " / 在庫:" + product.getProductStock());
        System.out.print("新しい商品名（Enterで変更なし）: ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = product.getProductName();
        }
        System.out.print("新しい価格（Enterで変更なし）: ");
        String priceInput = scanner.nextLine();
        int price = priceInput.isEmpty() ? product.getProductPrice() : Integer.parseInt(priceInput);
        System.out.print("新しい在庫数（Enterで変更なし）: ");
        String stockInput = scanner.nextLine();
        int stock = stockInput.isEmpty() ? product.getProductStock() : Integer.parseInt(stockInput);

        boolean ok = productService.updateProduct(id, name, price, stock);
        System.out.println(ok ? "更新しました。" : "更新に失敗しました。");
    }

    // 商品論理削除（販売終了）
    private void handleDeactivateProduct() {
        System.out.print("販売終了にしたい商品のID: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean ok = productService.deleteProduct(id);
        System.out.println(ok ? "商品ID " + id + " を販売終了にしました。" : "該当する商品がありません。");
    }

    //統計メニュー(仮)
//    *グラフがごちゃごちゃ無理かも
    private void showReportMenu() {
        while (true) {
            System.out.println("\n=== レポートメニュー ===");
            System.out.println("1. 商品統計");
            System.out.println("2. 売上統計");
            System.out.println("3. 顧客統計");
            System.out.println("4. 顧客統計");
            System.out.println("5. 商品売上ランキング");
            System.out.println("6. 顧客金額ランキング");
            System.out.println("7. 管理者メニューに戻る");
            System.out.print("番号を選んでください: ");

            String input = scanner.nextLine();
            switch (input) {
            case "1":
                printProductStats();
                break;
            case "2":
                printProductSalesStats();
                break;
            case "3":
                printUserStatsById();
                break;
            case "4":
                printUserStatsBySpent();
                break;
            case "5":
                printTop5ProductsBySales();
                break;
            case "6":
                printTop5UsersBySpent();
                break;
            case "7":
                return;
            default:
                System.out.println("不正な入力です。");
            }
        }
    }

    // 商品統計
    private void printProductStats() {
        List<ProductStats> list = reportService.getProductStats();
        System.out.println("\n=== 商品統計 ===");
        System.out.printf("ID | 名前 | 全体数 | 在庫 | 売れた数 | 割合 | active | 登録日時%n");
        for (ProductStats stats : list) {
            System.out.printf(
                    "%d | %s | %d | %d | %d | %.2f | %b | %s%n",
                    stats.getProductId(),
                    stats.getName(),
                    stats.getTotalCount(),
                    stats.getStock(),
                    stats.getSoldCount(),
                    stats.getSoldRatio(),
                    stats.isActive(),
                    stats.getCreatedAt()
            );
        }
    }

    // 売上統計
    private void printProductSalesStats() {
        List<ProductSalesStats> list = reportService.getProductSalesStats();
        System.out.println("\n=== 売上統計（商品別） ===");
        System.out.printf("ID | 名前 | 価格 | 売れた数 | 売上合計 | 売上割合%n");
        for (ProductSalesStats stats : list) {
            System.out.printf(
                    "%d | %s | %d | %d | %d | %.2f%n",
                    stats.getProductId(),
                    stats.getName(),
                    stats.getPrice(),
                    stats.getSoldCount(),
                    stats.getSalesAmount(),
                    stats.getSalesRatio()
            );
        }
    }

    // 顧客統計
    private void printUserStatsById() {
        List<UserStats> list = reportService.getUserStatsById();
        System.out.println("\n=== 顧客統計（ID順） ===");
        System.out.printf("ID | 名前 | 住所 | email | 使った金額 | 購入回数 | 売上割合 | 権限 | 登録日時%n");
        for (UserStats stats : list) {
            System.out.printf(
                    "%d | %s | %s | %s | %d | %d | %.2f | %s | %s%n",
                    stats.getUserId(),
                    stats.getName(),
                    stats.getAddress(),
                    stats.getEmail(),
                    stats.getTotalSpent(),
                    stats.getOrderCount(),
                    stats.getSalesRatio(),
                    stats.getRole(),
                    stats.getCreatedAt()
            );
        }
    }

    // 顧客統計
    private void printUserStatsBySpent() {
        List<UserStats> list = reportService.getUserStatsBySpentDesc();
        System.out.println("\n=== 顧客統計（使った金額順） ===");
        System.out.printf("ID | 名前 | 住所 | email | 使った金額 | 購入回数 | 売上割合 | 権限 | 登録日時%n");
        for (UserStats stats : list) {
            System.out.printf(
                    "%d | %s | %s | %s | %d | %d | %.2f | %s | %s%n",
                    stats.getUserId(),
                    stats.getName(),
                    stats.getAddress(),
                    stats.getEmail(),
                    stats.getTotalSpent(),
                    stats.getOrderCount(),
                    stats.getSalesRatio(),
                    stats.getRole(),
                    stats.getCreatedAt()
            );
        }
    }

    // 商品売上ランキング
    private void printTop5ProductsBySales() {
        List<ProductSalesStats> list = reportService.getTop5ProductsBySales();
        System.out.println("\n=== 商品売上ランキング Top5 ===");
        System.out.printf("順位 | ID | 名前 | 売上合計 | 売れた数%n");
        int rank = 1;
        for (ProductSalesStats stats : list) {
            System.out.printf(
                    "%d位 | %d | %s | %d | %d%n",
                    rank++,
                    stats.getProductId(),
                    stats.getName(),
                    stats.getSalesAmount(),
                    stats.getSoldCount()
            );
        }
    }

    // 顧客金額ランキング
    private void printTop5UsersBySpent() {
        List<UserStats> list = reportService.getTop5UsersBySpent();
        System.out.println("\n=== 顧客金額ランキング Top5 ===");
        System.out.printf("順位 | ID | 名前 | 使った金額 | 購入回数%n");
        int rank = 1;
        for (UserStats stats: list) {
            System.out.printf(
                    "%d位 | %d | %s | %d | %d%n",
                    rank++,
                    stats.getUserId(),
                    stats.getName(),
                    stats.getTotalSpent(),
                    stats.getOrderCount()
            );
        }
    }

    //ユーザーメニュー(仮)
    //＊つまらない、動くよう
    private void showUserMenu() {
        User user = authService.getLoggedInUser();

        while (true) {
            System.out.println("\n=== 一般ユーザーメニュー ===");
            System.out.println("1. 商品一覧を見る");
            System.out.println("2. カートに商品を追加");
            System.out.println("3. カートの中身を見る");
            System.out.println("4. カートから商品を削除");
            System.out.println("5. 注文を確定する");
            System.out.println("6. ログアウトしてメインメニューに戻る");
            System.out.print("番号を選んでください: ");

            String input = scanner.nextLine();

            switch (input) {
            case "1":
                showProducts(productService.getActiveProducts(), "販売中の商品");
                break;
            case "2":
                handleAddToCart(user);
                break;
            case "3":
                handleShowCart(user);
                break;
            case "4":
                handleRemoveFromCart(user);
                break;
            case "5":
                handleCheckout(user);
                break;
            case "6":
                System.out.println("ログアウトします。");
                authService.logout();
                return;
            default:
                System.out.println("不正な入力です。");
            }
        }
    }

    //カートに追加
    //*強制的に商品一覧もあり
    //id追加っておかしい名前がいい
    private void handleAddToCart(User user) {
        System.out.print("商品ID: ");
        int productId = Integer.parseInt(scanner.nextLine());
        System.out.print("数量: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        boolean ok = cartService.addToCart(user, productId, quantity);
        System.out.println(ok ? "カートに追加しました。" : "カートに追加できませんでした。（在庫不足 or 商品なし）");
    }

    //カート一覧
    private void handleShowCart(User user) {
        Cart cart = cartService.getCart(user);
        List<CartItem> items = cart.getItems();
        System.out.println("\n=== カート内容 ===");
        if (items.isEmpty()) {
            System.out.println("カートは空です。");
            return;
        }
        for (CartItem cartItem : items) {
            Product product = productService.getProductById(cartItem.getProductId());
            String name = (product != null) ? product.getProductName() : "(削除された商品)";
            System.out.printf("名前:%s | 数量:%d%n",
                    name, cartItem.getQuantity());
        }
    }

    //商品Idで指定して削除
    //ここもid追加はおかしい
    private void handleRemoveFromCart(User user) {
        System.out.print("カートから削除したい商品ID: ");
        int productId = Integer.parseInt(scanner.nextLine());
        cartService.removeFromCart(user, productId);
        System.out.println("カートから削除しました。");
    }

    //注文確定
    private void handleCheckout(User user) {
        Cart cart = cartService.getCart(user);
        if (cart.getItems().isEmpty()) {
            System.out.println("カートが空です。");
            return;
        }

        System.out.print("支払い方法 (1:クレジット / 2:銀行振込): ");
        int paymentMethod = Integer.parseInt(scanner.nextLine());

        System.out.print("配送先住所（Enter で会員登録住所を使用）: ");
        String inputAddress = scanner.nextLine();
        String shippingAddress = inputAddress.isEmpty() ? user.getAddress() : inputAddress;

        Order order = orderService.createOrderFromCart(user, cart, paymentMethod, shippingAddress);
        if (order == null) {
            System.out.println("注文を作成できませんでした。");
            return;
        }

        System.out.println("注文が確定しました。注文ID: " + order.getOrderId() +
                " / 合計金額: " + order.getTotalAmount() + " 円");
    }
}
