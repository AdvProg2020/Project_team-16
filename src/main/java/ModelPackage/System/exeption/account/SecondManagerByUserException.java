package ModelPackage.System.exeption.account;

public class SecondManagerByUserException extends Exception {
    public SecondManagerByUserException() {
        super("Only managers can make other managers.");
    }
}
