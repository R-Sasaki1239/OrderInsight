package orderinsight.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import orderinsight.model.Product;

public class InMemoryProductRepository implements ProductRepository{
	
	private final List<Product> products = new ArrayList<>();
	
	public InMemoryProductRepository() {
		products.add(new Product(1, "りんご", 120, 50, true));
		products.add(new Product(2, "バナナ", 80, 30, true));
		products.add(new Product(3, "いちご", 150, 0, false));
		products.add(new Product(4, "ぶどう", 400, 200, true));
		products.add(new Product(5, "すいか", 280, 100, true));
		products.add(new Product(6, "ビール", 3000, 120, false));
		products.add(new Product(7, "トマト", 180, 80, true));
		products.add(new Product(8, "いわし", 200, 1200, false));
		products.add(new Product(9, "たまご", 360, 1040, true));
		products.add(new Product(10, "白菜", 250, 123, true));
		products.add(new Product(11, "牛肉", 300, 290, false));
		products.add(new Product(12, "羊肉", 400, 39, true));
	}
	
	@Override
	public Product findById(int productId) {
		return products.stream()
				.filter(p -> p.getProductId() == productId)
				.findFirst()
				.orElse(null);
	}
	
	@Override
	public List<Product> findAll(){
		return new ArrayList<>(products);
	}
	
	@Override
	public void save(Product product) {
		Optional<Product> existingOpt = products.stream()
				.filter(p -> p.getProductId() == product.getProductId())
				.findFirst();
		
		if (existingOpt.isPresent()) {
			Product existing = existingOpt.get();
			products.remove(existing);
		}
		products.add(product);
	}
	
	@Override
	public void deleteById(int productId) {
		products.removeIf(p -> p.getProductId() == productId);
	}
}
