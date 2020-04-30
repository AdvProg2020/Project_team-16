package ModelPackage.System.exeption.discount;

public class UserExistedInDiscountCodeException extends Exception {
    public UserExistedInDiscountCodeException(String userName) {
        super(String.format("User with username (%s) existed in discount code!", userName));
    }
}
