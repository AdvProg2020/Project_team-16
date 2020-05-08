package ModelPackage.System.exeption.product;

public class AlreadyASeller extends Exception{
    public AlreadyASeller(String id){
        super(String.format("Id (%s) Is Already A Seller Of This Product",id));
    }
}
