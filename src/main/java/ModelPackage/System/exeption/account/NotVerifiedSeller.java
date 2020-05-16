package ModelPackage.System.exeption.account;

public class NotVerifiedSeller extends Exception {
    public NotVerifiedSeller(){
        super("This Seller Isn't Verified Yet");
    }
}
