package orderinsight.service;

import java.util.List;
import java.util.stream.Collectors;

import orderinsight.model.Product;
import orderinsight.util.ProductRepository;

public class ProductService {
	private final ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	//商品一覧
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	//販売中の商品一覧
	public List<Product> getActiveProducts(){
		return productRepository.findAll().stream()
				.filter(Product::isActive)
				.collect(Collectors.toList());
	}
	
	//販売停止中の商品一覧
	public List<Product> getInactiveProducts(){
		return productRepository.findAll().stream()
				.filter(p -> !p.isActive())
				.collect(Collectors.toList());
	}
	
	//productIdで一件取得
	public Product getProductById(int productId) {
		return productRepository.findById(productId);
	}
	
	//productの新規登録
	public Product createProduct(String name, int price, int stock, boolean active) {
		int newId = productRepository.findAll().size() + 1;
		
		Product product = new Product(newId, name, price, stock, active);
		productRepository.save(product);
		return product;
	}
	
	//更新
	public boolean updateProduct(int productId, String name, int price, int stock) {
		Product existing = productRepository.findById(productId);
		if (existing == null) {
		    return false;
		}
		
		existing.setProductName(name);
		existing.setProductPrice(price);
		existing.setProductStock(stock);
		
		productRepository.save(existing);
		return true;
	}
	
	//	論理削除
	public boolean deleteProduct(int productId) {
		Product existing = productRepository.findById(productId);
		if (existing == null) {
		    return false;
		}
		existing.setActive(false);
		productRepository.save(existing);
		return true;
	}
}
