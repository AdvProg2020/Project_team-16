package ModelPackage.System.exeption.cart;

public class NotEnoughAmountOfProductException extends Exception {
    public NotEnoughAmountOfProductException(int currentAmount) {
        super(String.format("The product Amount is only (%d)!", currentAmount));
    }
}
