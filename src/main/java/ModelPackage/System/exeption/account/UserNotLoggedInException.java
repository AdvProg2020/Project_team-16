package ModelPackage.System.exeption.account;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException() {
        super("You must sign-in first.");
    }
}
