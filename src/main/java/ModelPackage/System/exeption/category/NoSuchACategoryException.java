package ModelPackage.System.exeption.category;

public class NoSuchACategoryException extends Exception{
    public NoSuchACategoryException(String id){
        super(String.format("There Isn't Such A Category With Id (%s)",id));
    }
}
