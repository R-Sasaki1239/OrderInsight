package orderinsight.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	
	private int userId;
	private List <CartItem> items;
	 
	 public Cart(int userId) {
		 this.userId = userId;
		 this.items = new ArrayList<>();
	 }
	 
	 public int getUserId() {
		 return userId;
	 }
	 
	 public List<CartItem> getItems(){
		 return items;
	 }
	 
	 //カートに商品一件分を追加(すでにあるなら数だけ編集)
	 public void addItem(int productId, int quantity) {
		 for(CartItem item : items) {
			if(item.getProductId() == productId) {
				 item.addQuantity(quantity);
				 return;
			}
		 }
		items.add(new CartItem(productId, quantity));
	 }
	 
	 //カートから商品Idで削除
	 public void removeItem(int productId) {
		items.removeIf(item -> item.getProductId() == productId);
	 }
	 
	 //カートの中身を空にする
	 public void clear() {
		 items.clear();
	 }
}