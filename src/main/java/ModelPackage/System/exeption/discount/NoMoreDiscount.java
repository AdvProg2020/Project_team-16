package ModelPackage.System.exeption.discount;

public class NoMoreDiscount extends Exception{
    public NoMoreDiscount(){
        super("This DiscountCode Is No Longer Available For This User");
    }
}
