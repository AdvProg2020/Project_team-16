package ModelPackage.System;

import ModelPackage.Users.Cart;
import ModelPackage.Users.SubCart;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartManager {
    private List<Cart> allCarts;
    private static CartManager cartManager = new CartManager();

    public static CartManager getInstance() {
        return cartManager;
    }

    private CartManager() {
        this.allCarts = new ArrayList<>();
    }

    public void addProductToCart(Cart cart, String sellerId, String productId, int amount) {
        cart.getSubCarts().add(new SubCart(ProductManager.getInstance().findProductById(productId),
                productId, sellerId, amount));
    }
}
