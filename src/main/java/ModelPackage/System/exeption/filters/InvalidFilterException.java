package ModelPackage.System.exeption.filters;

public class InvalidFilterException extends Exception {
    public InvalidFilterException(String filter){
        super("The Filter (" + filter + ") Is Not Valid For This Category");
    }
}
