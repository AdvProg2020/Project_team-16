package ModelPackage.System.exeption.cart;

public class NotTheSellerException extends Exception {
    public NotTheSellerException(){
        super("Seller You choose is not a seller of this product");
    }
}
