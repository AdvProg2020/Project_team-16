package ModelPackage.System;

import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotPositiveAmountProductException;
import ModelPackage.System.exeption.cart.ProductExistedInCart;
import ModelPackage.Users.Cart;
import ModelPackage.Users.SubCart;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartManager {
    private static CartManager cartManager = new CartManager();

    public static CartManager getInstance() {
        return cartManager;
    }

    private CartManager() {}

    public void addProductToCart(Cart cart, String sellerId, String productId, int amount) throws Exception {
        checkIfProductExists(cart, productId, sellerId);
        cart.getSubCarts().add(new SubCart(ProductManager.getInstance().findProductById(productId),
                productId, sellerId, amount));
    }

    private void checkIfProductExists(Cart cart, String productId, String sellerId) throws Exception{
        for (SubCart subCart : cart.getSubCarts()) {
            if (subCart.getSellerId().equals(sellerId) && subCart.getProductId().equals(productId))
                throw new ProductExistedInCart(productId, sellerId);
        }
    }

    public SubCart getSubCartByProductId(Cart cart, String productId, String sellerId) throws NoSuchAProductInCart {
        for (SubCart subCart : cart.getSubCarts()) {
            if (subCart.getProductId().equals(productId) && subCart.getSellerId().equals(sellerId))
                return subCart;
        }
        throw new NoSuchAProductInCart(productId, sellerId);
    }

    public void deleteProductFromCart(Cart cart, String productId, String sellerId) throws NoSuchAProductInCart {
        SubCart subCart = getSubCartByProductId(cart, productId, sellerId);
        cart.getSubCarts().remove(subCart);
    }

    public void changeProductAmountInCart(Cart cart, String productId, String sellerId, int newAmount)
            throws Exception {
        SubCart subCart = getSubCartByProductId(cart, productId, sellerId);
        checkIfAmountIsPositive(newAmount);
        subCart.setAmount(newAmount);
    }

    private void checkIfAmountIsPositive(int newAmount) throws Exception{
        if (newAmount <= 0)
            throw new NotPositiveAmountProductException(newAmount);
    }
}
