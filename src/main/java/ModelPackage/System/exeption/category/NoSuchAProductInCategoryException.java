package ModelPackage.System.exeption.category;

public class NoSuchAProductInCategoryException extends Exception {
    public NoSuchAProductInCategoryException(int productId,String categoryId){
        super(String.format("There Isn't Such A Product With Id (%s) In Category With Id (%s)",productId,categoryId));
    }
}
