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
		products.add(new Product(3, "オレンジ", 150, 0, false));
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
