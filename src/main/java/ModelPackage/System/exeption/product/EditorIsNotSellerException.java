package ModelPackage.System.exeption.product;

public class EditorIsNotSellerException extends Exception {
    public EditorIsNotSellerException(){
        super("Who tried to change this product is not a seller");
    }
}
