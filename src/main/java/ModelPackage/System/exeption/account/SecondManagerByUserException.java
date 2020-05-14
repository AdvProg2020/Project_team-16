package ModelPackage.System.exeption.account;

public class SecondManagerByUserException extends RuntimeException {
    public SecondManagerByUserException() {
        super("Only managers can make other managers.");
    }
}
