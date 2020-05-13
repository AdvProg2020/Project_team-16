package ModelPackage.System.exeption.account;

public class UserNotAvailableException extends RuntimeException {
    public UserNotAvailableException() {
        super("User with this username already exists.");
    }
}
