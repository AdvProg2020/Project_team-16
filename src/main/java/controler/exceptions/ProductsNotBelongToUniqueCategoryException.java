package controler.exceptions;

public class ProductsNotBelongToUniqueCategoryException extends Exception {
    public ProductsNotBelongToUniqueCategoryException(int firstProductId, int secondProductId) {
        super(String.format("The product (%d) doesn't have same category with product (%s)!", firstProductId, secondProductId));
    }
}
