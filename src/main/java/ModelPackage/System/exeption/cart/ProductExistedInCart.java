package ModelPackage.System.exeption.cart;

public class ProductExistedInCart extends Exception {
    public ProductExistedInCart(String productId, String sellerId) {
        super(String.format("Product with id (%s) with seller id (%s) exists in cart!", productId, sellerId));
    }
}
