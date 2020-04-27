package ModelPackage.System.exeption.account;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String username) {
        super(String.format("Password is incorrect for '%s'" , username));
    }
}
