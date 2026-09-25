package orderinsight;

import orderinsight.model.Product;
import orderinsight.util.InMemoryProductRepository;
import orderinsight.util.ProductRepository;

public class TestProductRepositoryMain {
    public static void main(String[] args) {
        ProductRepository repo = new InMemoryProductRepository();

        System.out.println("=== Product 全件 ===");
        for (Product p : repo.findAll()) {
            System.out.printf("ID:%d, 名前:%s, 価格:%d, 在庫:%d, active:%b%n",
                    p.getProductId(),
                    p.getProductName(),
                    p.getProductPrice(),
                    p.getProductStock(),
                    p.isActive());
        }

        System.out.println("\n=== ID=1 を取得 ===");
        Product p1 = repo.findById(1);
        System.out.println(p1 != null ? "見つかった: " + p1.getProductName() : "見つからない");
    }
}
