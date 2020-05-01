package ModelPackage.System.exeption.cart;

public class NoSuchAProductInCart extends Exception{
    public NoSuchAProductInCart(String productId, String sellerId) {
        super(String.format("There is no product with id (%s) with sellerId (%s) in cart!", productId, sellerId));
    }
}
