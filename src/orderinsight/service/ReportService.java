package orderinsight.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import orderinsight.model.Order;
import orderinsight.model.OrderItem;
import orderinsight.model.Product;
import orderinsight.model.User;
import orderinsight.model.report.ProductSalesStats;
import orderinsight.model.report.ProductStats;
import orderinsight.model.report.UserStats;
import orderinsight.util.OrderItemRepository;
import orderinsight.util.OrderRepository;
import orderinsight.util.ProductRepository;
import orderinsight.util.UserRepository;
//わからん
public class ReportService {
	
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ReportService(OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ---------------- 商品統計 ----------------

    public List<ProductStats> getProductStats() {
        List<Product> products = productRepository.findAll();
        List<OrderItem> orderItems = orderItemRepository.findAll();

        // productId -> 売れた数量
        Map<Integer, Integer> soldCountMap = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            int productId = orderItem.getItemId();
            soldCountMap.merge(productId, orderItem.getItemQuantity(), Integer::sum);
        }

        List<ProductStats> result = new ArrayList<>();
        for (Product p : products) {
            int productId = p.getProductId();
            int stock = p.getProductStock();
            int sold = soldCountMap.getOrDefault(productId, 0);
            int totalCount = stock + sold;
            double ratio = (totalCount > 0) ? (double) sold / totalCount : 0.0;
            String createdAt = p.getCreatedAt().format(dtf);

            result.add(new ProductStats(
                    productId,
                    p.getProductName(),
                    totalCount,
                    stock,
                    sold,
                    ratio,
                    p.isActive(),
                    createdAt
            ));
        }

        // ID順
        result.sort(Comparator.comparingInt(ProductStats::getProductId));
        return result;
    }

    // ---------------- 商品売上統計 ----------------

    public List<ProductSalesStats> getProductSalesStats() {
        List<Product> products = productRepository.findAll();
        List<OrderItem> orderItems = orderItemRepository.findAll();

        // productId -> 売れた数量 & 売上
        Map<Integer, Integer> soldCountMap = new HashMap<>();
        Map<Integer, Integer> salesAmountMap = new HashMap<>();
        for (OrderItem oi : orderItems) {
            int productId = oi.getItemId();
            soldCountMap.merge(productId, oi.getItemQuantity(), Integer::sum);
            salesAmountMap.merge(productId, oi.getLineTotal(), Integer::sum);
        }

        int totalSales = salesAmountMap.values().stream().mapToInt(Integer::intValue).sum();

        List<ProductSalesStats> result = new ArrayList<>();
        for (Product p : products) {
            int productId = p.getProductId();
            int sold = soldCountMap.getOrDefault(productId, 0);
            int sales = salesAmountMap.getOrDefault(productId, 0);
            double ratio = (totalSales > 0) ? (double) sales / totalSales : 0.0;

            result.add(new ProductSalesStats(
                    productId,
                    p.getProductName(),
                    p.getProductPrice(),
                    sold,
                    sales,
                    ratio
            ));
        }

        // ID順
        result.sort(Comparator.comparingInt(ProductSalesStats::getProductId));
        return result;
    }

    // ---------------- 顧客統計 ----------------

    // ID順
    public List<UserStats> getUserStatsById() {
        List<User> users = userRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        // userId -> 金額・回数
        Map<Integer, Integer> spentMap = new HashMap<>();
        Map<Integer, Integer> countMap = new HashMap<>();
        for (Order o : orders) {
            int userId = o.getOrderUserId();
            spentMap.merge(userId, o.getTotalAmount(), Integer::sum);
            countMap.merge(userId, 1, Integer::sum);
        }

        int totalSales = spentMap.values().stream().mapToInt(Integer::intValue).sum();

        List<UserStats> result = new ArrayList<>();
        for (User u : users) {
            int userId = u.getUserId();
            int spent = spentMap.getOrDefault(userId, 0);
            int count = countMap.getOrDefault(userId, 0);
            double ratio = (totalSales > 0) ? (double) spent / totalSales : 0.0;
            String createdAt = u.getCreatedAt().format(dtf);

            result.add(new UserStats(
                    userId,
                    u.getName(),
                    u.getAddress(),
                    u.getEmail(),
                    spent,
                    count,
                    ratio,
                    u.getRole(),
                    createdAt
            ));
        }

        result.sort(Comparator.comparingInt(UserStats::getUserId));
        return result;
    }

    // 使った金額順（降順）
    public List<UserStats> getUserStatsBySpentDesc() {
        List<UserStats> list = getUserStatsById();
        return list.stream()
                .sorted(Comparator.comparingInt(UserStats::getTotalSpent).reversed())
                .collect(Collectors.toList());
    }

    // ---------------- ランキング Top5 ----------------

    // 商品の売上金額ランキング Top5
    public List<ProductSalesStats> getTop5ProductsBySales() {
        return getProductSalesStats().stream()
                .sorted(Comparator.comparingInt(ProductSalesStats::getSalesAmount).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    // ユーザーの利用金額ランキング Top5
    public List<UserStats> getTop5UsersBySpent() {
        return getUserStatsBySpentDesc().stream()
                .limit(5)
                .collect(Collectors.toList());
    }
}

