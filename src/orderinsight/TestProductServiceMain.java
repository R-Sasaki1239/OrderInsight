package orderinsight;

import java.util.List;

import orderinsight.model.Product;
import orderinsight.service.ProductService;
import orderinsight.util.InMemoryProductRepository;
import orderinsight.util.ProductRepository;

public class TestProductServiceMain {
    public static void main(String[] args) {

        // ① リポジトリ & サービス準備
        ProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepository);

        // ② 初期状態の確認（InMemoryProductRepository の初期データ）
        System.out.println("=== 初期: 全商品 ===");
        printProducts(productService.getAllProducts());

        System.out.println("\n=== 初期: 販売中の商品 ===");
        printProducts(productService.getActiveProducts());

        System.out.println("\n=== 初期: 販売停止中の商品 ===");
        printProducts(productService.getInactiveProducts());

        // ③ 新規登録のテスト
        System.out.println("\n=== createProduct で新規登録 ===");
        Product newProduct = productService.createProduct("ぶどう", 200, 40, true);
        System.out.println("追加された商品: ID=" + newProduct.getProductId() + ", name=" + newProduct.getProductName());

        System.out.println("\n=== 追加後: 全商品 ===");
        printProducts(productService.getAllProducts());

        // ④ 更新のテスト
        System.out.println("\n=== updateProduct で更新（ぶどうの価格と在庫変更） ===");
        boolean updated = productService.updateProduct(newProduct.getProductId(), "ぶどう(大粒)", 250, 25);
        System.out.println("更新結果: " + updated);

        Product updatedProduct = productService.getProductById(newProduct.getProductId());
        System.out.println("更新後の商品: "
                + updatedProduct.getProductName() + ", 価格=" + updatedProduct.getProductPrice()
                + ", 在庫=" + updatedProduct.getProductStock());

        // ⑤ 論理削除のテスト
        System.out.println("\n=== deleteProduct (論理削除) を実行 ===");
        boolean deleted = productService.deleteProduct(newProduct.getProductId());
        System.out.println("削除結果: " + deleted);

        System.out.println("\n=== 削除後: 販売中の商品 ===");
        printProducts(productService.getActiveProducts());

        System.out.println("\n=== 削除後: 販売停止中の商品 ===");
        printProducts(productService.getInactiveProducts());
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("商品がありません。");
            return;
        }
        for (Product p : products) {
            System.out.printf(
                    "ID:%d | name:%s | price:%d | stock:%d | active:%b%n",
                    p.getProductId(),
                    p.getProductName(),
                    p.getProductPrice(),
                    p.getProductStock(),
                    p.isActive());
        }
    }
}