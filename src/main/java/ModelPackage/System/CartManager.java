package ModelPackage.System;

import ModelPackage.Product.Product;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.cart.NotPositiveAmountProductException;
import ModelPackage.System.exeption.cart.ProductExistedInCart;
import ModelPackage.Users.Cart;
import ModelPackage.Users.SubCart;
import lombok.Data;

@Data
public class CartManager {
    private static CartManager cartManager = new CartManager();

    public static CartManager getInstance() {
        return cartManager;
    }

    private CartManager() {}

    public void addProductToCart(Cart cart, String sellerId, int productId, int amount) throws Exception {
        checkIfProductExistsInCart(cart, productId, sellerId);
        checkIfThereIsEnoughAmountOfProduct(productId, sellerId, amount);
        cart.getSubCarts().add(new SubCart(ProductManager.getInstance().findProductById(productId),
                productId, sellerId, amount));
        cart.setTotalPrice(calculateTotalPrice(cart));
    }

    private long calculateTotalPrice(Cart cart) {
        long total = 0;
        for (SubCart subCart : cart.getSubCarts()) {
            total += subCart.getAmount() * subCart.getProduct().getPrices().get(subCart.getSellerId());
        }
        return total;
    }

    private void checkIfThereIsEnoughAmountOfProduct(int productId, String sellerId, int amount)
    throws Exception {
        Product product = ProductManager.getInstance().findProductById(productId);
        if (product.getStock().get(sellerId) < amount) {
            throw new NotEnoughAmountOfProductException(product.getStock().get(sellerId));
        }
    }

    private void checkIfProductExistsInCart(Cart cart, int productId, String sellerId) throws Exception{
        for (SubCart subCart : cart.getSubCarts()) {
            if (subCart.getSellerId().equals(sellerId) && subCart.getId().equals(productId))
                throw new ProductExistedInCart(productId, sellerId);
        }
    }

    public SubCart getSubCartByProductId(Cart cart, int productId, String sellerId) throws NoSuchAProductInCart {
        for (SubCart subCart : cart.getSubCarts()) {
            if (subCart.getId().equals(productId) && subCart.getSellerId().equals(sellerId))
                return subCart;
        }
        throw new NoSuchAProductInCart(productId, sellerId);
    }

    public void deleteProductFromCart(Cart cart, int productId, String sellerId) throws NoSuchAProductInCart {
        SubCart subCart = getSubCartByProductId(cart, productId, sellerId);
        cart.getSubCarts().remove(subCart);
    }

    public void changeProductAmountInCart(Cart cart, int productId, String sellerId, int newAmount)
            throws Exception {
        SubCart subCart = getSubCartByProductId(cart, productId, sellerId);
        checkIfThereIsEnoughAmountOfProduct(productId, sellerId, newAmount);
        checkIfAmountIsPositive(newAmount);
        subCart.setAmount(newAmount);
        cart.setTotalPrice(calculateTotalPrice(cart));
    }

    private void checkIfAmountIsPositive(int newAmount) throws Exception{
        if (newAmount <= 0)
            throw new NotPositiveAmountProductException(newAmount);
    }
}
