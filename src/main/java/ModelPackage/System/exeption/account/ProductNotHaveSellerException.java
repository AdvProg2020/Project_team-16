package ModelPackage.System.exeption.account;

public class ProductNotHaveSellerException extends Exception {
    public ProductNotHaveSellerException(int productId, String sellerUserName) {
        super(String.format("Product with id (%d) doesn't have seller (%s)!", productId, sellerUserName));
    }
}
