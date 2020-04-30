package ModelPackage.System;

import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
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

    public SubCart getSubCartByProductId(Cart cart, String productId) throws NoSuchAProductInCart {
        for (SubCart subCart : cart.getSubCarts()) {
            if (subCart.getProductId().equals(productId))
                return subCart;
        }
        throw new NoSuchAProductInCart(productId);
    }

    public void deleteProductFromCart(Cart cart, String productId) throws NoSuchAProductInCart {
        SubCart subCart = getSubCartByProductId(cart, productId);
        cart.getSubCarts().remove(subCart);
    }

    public void changeProductAmountInCart(Cart cart, String productId, int newAmount) throws NoSuchAProductInCart {
        SubCart subCart = getSubCartByProductId(cart, productId);
        subCart.setAmount(newAmount);
    }
}
