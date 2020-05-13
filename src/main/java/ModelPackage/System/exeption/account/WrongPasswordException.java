package ModelPackage.System.exeption.account;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String username) {
        super(String.format("Password is incorrect for '%s'" , username));
    }
}
