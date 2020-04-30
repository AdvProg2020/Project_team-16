package ModelPackage.System.exeption.account;

public class UserNotAvailableException extends Exception {
    public UserNotAvailableException() {
        super("User with this username is not available.");
    }
}
