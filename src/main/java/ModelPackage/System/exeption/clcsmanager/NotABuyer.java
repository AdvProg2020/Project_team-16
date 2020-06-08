package ModelPackage.System.exeption.clcsmanager;

public class NotABuyer extends Exception{
    public NotABuyer(){
        super("You Didn't buy this product");
    }
}
