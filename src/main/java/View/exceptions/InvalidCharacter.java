package View.exceptions;

public class InvalidCharacter extends Exception{
    public InvalidCharacter(){
        super("Characters are invalid.They only must include A-Z a-z underscore and numbers");
    }
}
