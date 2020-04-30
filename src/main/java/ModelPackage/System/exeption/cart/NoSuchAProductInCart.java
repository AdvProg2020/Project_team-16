package ModelPackage.System.exeption.cart;

public class NoSuchAProductInCart extends Exception{
    public NoSuchAProductInCart(String productId) {
        super(String.format("There is no product with id (%s) in cart!", productId));
    }
}
