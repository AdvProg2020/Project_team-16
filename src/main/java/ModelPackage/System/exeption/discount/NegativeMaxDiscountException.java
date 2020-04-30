package ModelPackage.System.exeption.discount;

public class NegativeMaxDiscountException extends Exception {
    public NegativeMaxDiscountException(long maxDiscount) {
        super(String.format("Your new MaxDiscount (%d) is negative!", maxDiscount));
    }
}
