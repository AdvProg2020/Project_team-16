package ModelPackage.System.exeption.cart;

public class ProductExistedInCart extends Exception {
    public ProductExistedInCart(int productId) {
        super(String.format("Product with id (%s) exists in cart!", productId));
    }
}
