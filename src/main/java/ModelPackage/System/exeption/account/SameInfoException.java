package ModelPackage.System.exeption.account;

public class SameInfoException extends Exception {
    public SameInfoException(String type) {
        super(String.format("The new %s you entered is the same as your old %s. Enter a different %s.", type, type, type));
    }
}
