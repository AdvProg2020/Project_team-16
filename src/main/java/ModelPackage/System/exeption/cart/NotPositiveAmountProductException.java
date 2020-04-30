package ModelPackage.System.exeption.cart;

public class NotPositiveAmountProductException extends Exception{
    public NotPositiveAmountProductException(int amount) {
        super(String.format("Your entered amount (%d) is not positive!", amount));
    }
}
