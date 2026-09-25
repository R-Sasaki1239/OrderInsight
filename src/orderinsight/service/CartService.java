package orderinsight.service;

import java.util.HashMap;
import java.util.Map;

import orderinsight.model.Cart;
import orderinsight.model.CartItem;
import orderinsight.model.Product;
import orderinsight.model.User;
import orderinsight.util.ProductRepository;

public class CartService {
	private final ProductRepository productRepository;
	
	private final Map<Integer, Cart> cartMap = new HashMap<>();
	
	public CartService(ProductRepository productRepository) {
	    this.productRepository = productRepository;
	}
	
	//よくわからん(カートを取得かなければ作る)
	public Cart getOrCreateCart(User user) {
		Cart cart = cartMap.get(user.getUserId());
		if (cart == null) {
			cart = new Cart(user.getUserId());
			cartMap.put(user.getUserId(), cart);
		}
		return cart;
	}
	
	//カートに商品を追加
	public boolean addToCart(User user, int productId, int quantity) {
		if (quantity <= 0) {
			return false;
		}
		
		Product product = productRepository.findById(productId);
		if (product == null || !product.isActive()) {
			return false; 
		}
		
		Cart cart = getOrCreateCart(user);
		
		//わからん(カート内での在庫の上限確認)
		int currentQuantityInCart = 0;
		for (CartItem cartitem : cart.getItems()) {
			if (cartitem.getProductId() == productId) {
				currentQuantityInCart = cartitem.getQuantity();
				break;
			}
		}
		if (currentQuantityInCart + quantity > product.getProductStock()) {
		    return false;
		}
		
		cart.addItem(productId, quantity);
		return true;
	}
	
	//カート取得
	public Cart getCart(User user) {
		return getOrCreateCart(user);
	}

	public void removeFromCart(User user, int productId) {
		Cart cart = cartMap.get(user.getUserId());
		if (cart != null) {
			cart.removeItem(productId);
		}
	}
	
	public void clearCart(User user) {
		Cart cart = cartMap.get(user.getUserId());
		if (cart != null) {
			cart.clear();
		}
	}
}