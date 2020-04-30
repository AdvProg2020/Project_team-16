package ModelPackage.System.exeption.discount;

public class NoSuchADiscountCodeException extends Exception{
    public NoSuchADiscountCodeException(String code) {
        super(String.format("There Isn't Such A Discount Code With Code (%s)",code));
    }
}
