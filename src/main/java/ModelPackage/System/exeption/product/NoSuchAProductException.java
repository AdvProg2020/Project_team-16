package ModelPackage.System.exeption.product;

public class NoSuchAProductException extends Exception{
    public NoSuchAProductException(String id){
        super(String.format("There Isn't Any Product With Id (%s)",id));
    }
}
