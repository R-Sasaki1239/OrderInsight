package orderinsight.util;

import java.util.List;

import orderinsight.model.Product;

public interface ProductRepository {
	Product findById(int productId);
	List<Product> findAll();
	void save(Product product);
	void deleteById(int productId);
}
