package orderinsight.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import orderinsight.model.Cart;
import orderinsight.model.CartItem;
import orderinsight.model.Order;
import orderinsight.model.OrderItem;
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
		String email;
		while(true){
			System.out.println("メールアドレスを入力してください");
			email = scanner.nextLine();
			
			boolean emailCheck = authService.emailCheck(email);
			if (emailCheck){
				break;
			}
			System.out.println("そのメールアドレスは登録されていません。もう一度入力してください");
		}
		System.out.println("パスワードを入力してください");
		String password = scanner.nextLine();
		boolean match = authService.login(email, password);
		if (!match) {
			System.out.println("パスワードが違います。");
			return;
		}
		
		User user = authService.getLoggedInUser();
		System.out.println("ようこそ、" + user.getName() + "さん");
		
		if (authService.isAdmin()) {
			showAdminMenu();
		} else {
			showUserMenu();
		}
	}
	
	//新規登録
	public void handleRegister() {
		String name;
		while(true){
			System.out.print("名前: ");
			name = scanner.nextLine();
			if (!name.isEmpty()) {
				break;
			}
			System.out.println("名前は必須です。もう一度入力してください。");
		}
		
		System.out.print("ニックネーム: ");
		String nickname = scanner.nextLine();
		
		String email;
		while(true){
			System.out.print("メールアドレス: ");
			email = scanner.nextLine();
			if (!email.isEmpty()) {
				break;
			}
			System.out.println("メールアドレスは必須です。もう一度入力してください。");
		}
		
		String password1;
		String password2;
		while(true) {
			System.out.print("パスワード: ");
			password1 = scanner.nextLine();
			System.out.print("パスワード（確認用）: ");
			password2 = scanner.nextLine();
			if (password1.equals(password2)) {
				break;
			}
			System.out.println("パスワードが一致しません。もう一度やり直してください。");
		}
		
		String address;
		while(true){
			System.out.print("住所: ");
			address = scanner.nextLine();
			if (!address.isEmpty()) {
				break;
			}
			System.out.println("住所が空です。もう一度入力してください。");
		}
		
		User user = authService.register(name, nickname, password1, address, email);
		System.out.println("登録が完了しました。");
		System.out.println("ようこそ " + user.getName() + " さん");
	
		authService.login(email, password1);
		if (authService.isAdmin()) {
			showAdminMenu();
		} else {
			showUserMenu();
		}
	}

    //管理者メニュー
	//つまらない、動く用
    private void showAdminMenu() {
        while (true) {
            System.out.println("\n=== 管理者メニュー ===");
            System.out.println("1. 全商品一覧を表示");
            System.out.println("2. 販売中の商品一覧を表示");
            System.out.println("3. 販売停止中の商品一覧を表示");
            System.out.println("4. 商品を新規登録");
            System.out.println("5. 商品情報を更新");
            System.out.println("6. 商品を販売終了");
            System.out.println("7. 全注文を一覧表示");
            System.out.println("8. 注文の明細を見る");
            System.out.println("9. レポートメニュー");
            System.out.println("10. ログアウトしてメインメニューに戻る");
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
            	handleShowAllOrders();
            	break;
            case "8":
            	handleShowOrderItems();
            	break;
            case "9":
                showReportMenu();
                break;
            case "10":
                System.out.println("ログアウトします。");
                authService.logout();
                return;
            default:
                System.out.println("不正な入力です。");
            }
        }
    }

    //商品一覧(admin用)
    private void showProducts(List<Product> products, String title) {
        System.out.println("\n=== " + title + " ===");
        if (products.isEmpty()) {
            System.out.println("商品がありません。");
            return;
        }
        for (Product product : products) {
        	String stockLabel;
        	if (product.getProductStock() == 0) {
				stockLabel = "売り切れ";
			} else {
				stockLabel = String.valueOf(product.getProductStock());
			}
        	String salesStatus;
        	if (product.isActive()) {
				salesStatus = "販売中";
			} else {
				salesStatus = "販売停止中";
			}
            System.out.printf(
                    "ID:%d | 名前:%s | 価格:%d | 在庫:%s | 販売状況:%s%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductPrice(),
                    stockLabel,
                    salesStatus
            );
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
        System.out.print("販売中にしますか？ (1:はい / 2:いいえ): ");
        boolean active = "1".equals(scanner.nextLine());

        Product product = productService.createProduct(name, price, stock, active);
        System.out.println("商品を登録しました。ID = " + product.getProductId());
    }

    // 商品更新
    private void handleUpdateProduct() {
        System.out.println("商品を検索する方法を選択してください");
        System.out.println("1: 商品Id / 2: 商品名");
        int choice;
        try {
        	choice = Integer.parseInt(scanner.nextLine());
			
		} catch (NumberFormatException e) {
			System.out.println("数字で入力してください");
			return;
		}
        
        Product product = null;
        
        if (choice == 1) {
			System.out.println("更新したい商品のId:");
			String idInput = scanner.nextLine();
			try {
				int id = Integer.parseInt(idInput);
				product = productService.getProductById(id);
			} catch (NumberFormatException e) {
				System.out.println("Idは数字で入力してください");
				return;
			}
		} else if (choice == 2) {
			System.out.println("更新したい商品の名前:");
			String nameInput = scanner.nextLine();
			product = productService.getProductByName(nameInput);
		} else {
			System.out.println("無効な選択です。");
			return;
		}
        
        if (product == null) {
			System.out.println("該当する商品がありません");
			return;
		}

        System.out.println("現在の情報: " + product.getProductName() + " / 価格:" + product.getProductPrice() 
        + " / 在庫:" + product.getProductStock()+ " / 販売状況:" + (product.isActive() ? "販売中" : "販売停止中"));
        System.out.print("新しい商品名（Enterで変更なし）: ");
        String newName = scanner.nextLine();
        if (newName.isEmpty()) {
            newName = product.getProductName();
        }
        System.out.print("新しい価格（Enterで変更なし）: ");
        String priceInput = scanner.nextLine();
        int newPrice;
        if(priceInput.isEmpty()) {
        	newPrice = product.getProductPrice();
        } else {
			try {
				newPrice = Integer.parseInt(priceInput);
			} catch (NumberFormatException e) {
				System.out.println("価格は数字で入力してください");
				return;
			}
		}
        System.out.print("新しい在庫数（Enterで変更なし）: ");
        String stockInput = scanner.nextLine();
        int newStock;
        if (stockInput.isEmpty()) {
			newStock = product.getProductStock();
		} else {
			try {
				newStock = Integer.parseInt(stockInput);
			} catch (NumberFormatException e) {
				System.out.println("在庫数は数字で入力してください");
				return;
			}
		}
        
        System.out.print("販売状況を変更しますか？ (1:販売中 / 2:販売停止 / Enterで変更なし): ");
        String activeInput = scanner.nextLine();
        boolean newActive;
        if (activeInput.isEmpty()) {
            newActive = product.isActive();
        } else if ("1".equals(activeInput)) {
            newActive = true;
        } else if ("2".equals(activeInput)) {
            newActive = false; 
        } else {
            System.out.println("不正な入力です。更新できませんでした。");
            newActive = product.isActive();
        }

        boolean ok = productService.updateProduct(product.getProductId(),newName, newPrice, newStock,newActive);
        System.out.println(ok ? "更新しました。" : "更新に失敗しました。");
    }

    // 商品論理削除（販売終了）
    private void handleDeactivateProduct() {
    	System.out.println("商品を検索する方法を選択してください");
        System.out.println("1: 商品Id / 2: 商品名");
        int choice;
        try {
        	choice = Integer.parseInt(scanner.nextLine());
			
		} catch (NumberFormatException e) {
			System.out.println("数字で入力してください");
			return;
		}
        
        Product product = null;
        
        if (choice == 1) {
			System.out.println("販売終了にしたい商品のId:");
			String idInput = scanner.nextLine();
			try {
				int id = Integer.parseInt(idInput);
				product = productService.getProductById(id);
			} catch (NumberFormatException e) {
				System.out.println("Idは数字で入力してください");
				return;
			}
		} else if (choice == 2) {
			System.out.println("販売終了にしたい商品の名前:");
			String nameInput = scanner.nextLine();
			product = productService.getProductByName(nameInput);
		} else {
			System.out.println("無効な選択です。");
			return;
		}
        
        if (product == null) {
			System.out.println("該当する商品がありません");
			return;
		}
        
        int id = product.getProductId();
        boolean ok = productService.deleteProduct(id);
        System.out.println(ok
                ? "商品「" + product.getProductName() + "」(ID " + id + ") を販売終了にしました。"
                : "該当する商品がありません。");
    }
    
    private void handleShowAllOrders() {
        List<Order> orders = orderService.getAllOrders();

        System.out.println("\n=== 全注文一覧 ===");
        if (orders.isEmpty()) {
            System.out.println("まだ注文がありません。");
            return;
        }

        for (Order order : orders) {
        	String formattedDateTime = order.getCreateDateTime().format(orderDtf);
            System.out.printf(
                    "注文ID:%d | ユーザーID:%d | 日時:%s | 金額:%d円 | 支払い方法:%d | 配送先:%s%n",
                    order.getOrderId(),
                    order.getOrderUserId(),
                    formattedDateTime,
                    order.getTotalAmount(),
                    order.getPaymentMethod(),
                    order.getShippingAddress()
            );
        }
    }
    
    private void handleShowOrderItems() {
    	System.out.println("明細をみたい注文を選択してください");
    	String idInput = scanner.nextLine();
    	int orderId;
    	try {
			orderId = Integer.parseInt(idInput);
		} catch (NumberFormatException e) {
			System.out.println("注文IDは数字で入力してください");
			return;
		}
    	
    	List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);
    	System.out.println("\n=== 注文ID " + orderId + " の明細 ===");
    	if (items.isEmpty()) {
    		System.out.println("この注文には明細がありません。");
    		return;
        }
    	for (OrderItem item : items) {
            Product p = productService.getProductById(item.getItemId());
            String name = (p != null) ? p.getProductName() : "(削除された商品)";

            System.out.printf(
                    "商品ID:%d | 名前:%s | 数量:%d | 小計:%d円%n",
                    item.getItemId(),
                    name,
                    item.getItemQuantity(),
                    item.getLineTotal()
            );
        }
    }

    //統計メニュー(仮)
//    *グラフがごちゃごちゃ無理かも
    private void showReportMenu() {
        while (true) {
            System.out.println("\n=== レポートメニュー ===");
            System.out.println("1. 商品統計");
            System.out.println("2. 売上統計");
            System.out.println("3. 顧客統計(ID順)");
            System.out.println("4. 顧客統計(金額順)");
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
        System.out.printf("ID | 名前 | 全体数 | 在庫 | 売れた数 | 売れた割合 | 販売状況 | 登録日時%n");
        for (ProductStats stats : list) {
        	String salesStatus;
            if (stats.isActive()) {
                salesStatus = "販売中";
            } else {
                salesStatus = "販売停止中";
            }
            System.out.printf(
                    "%d | %s | %d | %d | %d | %.2f%% | %s | %s%n",
                    stats.getProductId(),
                    stats.getName(),
                    stats.getTotalCount(),
                    stats.getStock(),
                    stats.getSoldCount(),
                    stats.getSoldRatio() * 100,
                    salesStatus,
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
                    "%d | %s | %d円 | %d | %d | %.2f%%%n",
                    stats.getProductId(),
                    stats.getName(),
                    stats.getPrice(),
                    stats.getSoldCount(),
                    stats.getSalesAmount(),
                    stats.getSalesRatio() * 100
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
                    "%d | %s | %s | %s%n  使った金額:%d円 | 購入回数:%d回 | 売り上げ割合:%.2f%% | %s | %s%n",
                    stats.getUserId(),
                    stats.getName(),
                    stats.getAddress(),
                    stats.getEmail(),
                    stats.getTotalSpent(),
                    stats.getOrderCount(),
                    stats.getSalesRatio() * 100,
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
                    "%d | %s | %s | %s%n  使った金額:%d円 | 購入回数:%d回 | 売り上げ割合:%.2f%% | %s | %s%n",
                    stats.getUserId(),
                    stats.getName(),
                    stats.getAddress(),
                    stats.getEmail(),
                    stats.getTotalSpent(),
                    stats.getOrderCount(),
                    stats.getSalesRatio() * 100,
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
            System.out.println("6. 注文履歴を見る");
            System.out.println("7. 注文履歴の明細を見る");
            System.out.println("8. ログアウトしてメインメニューに戻る");
            System.out.print("番号を半角で入力してください: ");

            String input = scanner.nextLine();

            switch (input) {
            case "1":
                showProducts(productService.getActiveProducts());
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
            	handleShowOrderHistory(user);
            	break;
            case "7":
            	handleShowOrderItemsForUser(user);
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
    
//    商品一覧(User用)
    private void showProducts(List<Product> products) {
		System.out.println("\n=== 販売中の商品 ===");
		if (products.isEmpty()) {
			System.out.println("販売中の商品はありません");
			return;
		}
		for (Product product : products) {
			String stockLabel;
			if (product.getProductStock() == 0) {
				stockLabel = "売り切れ";
			} else {
				stockLabel = String.valueOf(product.getProductStock());
			}
			System.out.printf(
					"名前:%s | 価格:%d | 在庫:%s%n",
                    product.getProductName(),
                    product.getProductPrice(),
                    stockLabel
					);
		}
	}

    //カートに追加
    private void handleAddToCart(User user) {
    	System.out.println("\n=== 販売中の商品 ===");
    	List<Product> products = productService.getActiveProducts();
		if (products.isEmpty()) {
			System.out.println("販売中の商品はありません");
			return;
		}
		for (Product product : products) {
			System.out.printf(
					"名前:%s | 価格:%d | 在庫:%d%n",
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getProductStock()
					);
		}
    	System.out.println("カートに入れる商品名を入力してください");
    	String name = scanner.nextLine();
    	
    	Product product = productService.getProductByName(name);
    	
    	if(product == null) {
    		System.out.println("その商品は見つかりませんでした。");
    		return;
    	}
    	if(!product.isActive()) {
    		System.out.println("その商品は販売停止しています。");
    		return;
    	}
    	
    	System.out.println("数量を入力してください");
    	int quantity;
    	try {
			quantity = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("数量は数字で入力してください");
			return;
		}
    	
    	boolean ok = cartService.addToCart(user, product.getProductId(), quantity);
    	if(ok) {
    		System.out.println(product.getProductName() + "を" + quantity + "個" + "カートに追加しました");
    	} else {
			System.out.println("在庫不足のため、カートに追加できませんでした。");
		}
	}

    //カート一覧
    private void handleShowCart(User user) {
        Cart cart = cartService.getOrCreateCart(user);
        List<CartItem> items = cart.getItems();
        System.out.println("\n=== カート内容 ===");
        if (items.isEmpty()) {
            System.out.println("カートは空です。");
            return;
        }
        
        int total = 0; 
        for (CartItem cartItem : items) {
            Product product = productService.getProductById(cartItem.getProductId());
            String name = (product != null) ? product.getProductName() : "(削除された商品)";
            int price = (product != null) ? product.getProductPrice() : 0;
            int quantity = cartItem.getQuantity();
            int subtotal = price * quantity;
            
            total += subtotal;
            System.out.printf("名前:%s | 単価:%d円 | 数量:%d | 小計:%d円%n",
                    name,
                    price,
                    quantity,
                    subtotal
            );
        }
        System.out.printf("=== 合計金額: %d円 ===%n", total);
    }

    //商品Idで指定して削除
    private void handleRemoveFromCart(User user) {
        System.out.print("カートから削除したい商品の名前: ");
        String productname = scanner.nextLine();
        
        Product product = productService.getProductByName(productname);
        if (product == null) {
			System.out.println("その名前の商品は見つかりません。");
			return;
		}
        
        int productId = product.getProductId();
        cartService.removeFromCart(user, productId);
        System.out.println("カートから削除しました。");
    }

    //注文確定
    private void handleCheckout(User user) {
        Cart cart = cartService.getOrCreateCart(user);
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
    
    public void handleShowOrderHistory(User user) {
    	List<Order> orders = orderService.getOrdersByUserList(user);
    	
    	System.out.println("\n=== 注文履歴一覧 ===");
    	if (orders.isEmpty()) {
			System.out.println("まだ注文履歴がありません");
			return;
		}
    	
    	for (Order order : orders) {
    		String formattedDateTime = order.getCreateDateTime().format(orderDtf);
			System.out.printf(
					"注文ID:%d | 日時:%s | 金額:%d円 | 支払い方法:%d | 配送先:%s%n",
					order.getOrderId(),
					formattedDateTime,
					order.getTotalAmount(),
					order.getPaymentMethod(),
					order.getShippingAddress()
					);
		}
    }
    
    private void handleShowOrderItemsForUser(User user) {
        System.out.println("明細を見たい注文IDを入力してください");
        String idInput = scanner.nextLine();
        int orderId;
        try {
            orderId = Integer.parseInt(idInput);
        } catch (NumberFormatException e) {
            System.out.println("注文IDは数字で入力してください");
            return;
        }
        
        Order order = orderService.getUserOrderItemsById(orderId);
        if (order == null) {
            System.out.println("その注文は存在しません。");
            return;
        }
        if (order.getOrderUserId() != user.getUserId()) {
            System.out.println("自分の注文ではありません。");
            return;
        }

        // 明細を取得
        List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);

        System.out.println("\n=== 注文ID " + orderId + " の明細 ===");
        if (items.isEmpty()) {
            System.out.println("この注文には明細がありません。");
            return;
        }

        for (OrderItem item : items) {
            Product p = productService.getProductById(item.getItemId());
            String name = (p != null) ? p.getProductName() : "(削除された商品)";

            System.out.printf(
                    "名前:%s | 数量:%d | 小計:%d円%n",
                    name,
                    item.getItemQuantity(),
                    item.getLineTotal()
            );
        }
    }
    
    private final DateTimeFormatter orderDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
