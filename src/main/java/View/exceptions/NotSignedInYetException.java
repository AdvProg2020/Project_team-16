package View.exceptions;

public class NotSignedInYetException extends Exception {
    public NotSignedInYetException(){
        super("You are NOT signed in and you don't have access to this menu");
    }
}
