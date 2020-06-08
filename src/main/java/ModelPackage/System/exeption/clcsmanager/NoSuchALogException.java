package ModelPackage.System.exeption.clcsmanager;

public class NoSuchALogException extends Exception {
    public NoSuchALogException(String id) {
        super(String.format("There Isn't Any Log With Id (%s)",id));
    }
}
