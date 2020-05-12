package ModelPackage.System.exeption.off;

public class NoSuchAOffException extends Exception{
    public NoSuchAOffException(int id){
        super(String.format("There Isn't Any Off With Id (%d)",id));
    }
}
