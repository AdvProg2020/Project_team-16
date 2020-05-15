package View.exceptions;

public class NotAnAvailableMenu extends Exception{
    public NotAnAvailableMenu(){
        super("This is not an available menu");
    }
}
