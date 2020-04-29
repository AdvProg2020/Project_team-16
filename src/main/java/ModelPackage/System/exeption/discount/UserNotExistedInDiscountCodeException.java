package ModelPackage.System.exeption.discount;

public class UserNotExistedInDiscountCodeException extends Exception {
    public UserNotExistedInDiscountCodeException(String userName) {
        super(String.format("User with username (%s) doesn't exist in discount code!", userName));
    }
}
