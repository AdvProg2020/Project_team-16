package ModelPackage.System.exeption.category;

public class RepeatedNameInParentCategoryException extends Exception {
    public RepeatedNameInParentCategoryException(String name) {
        super(String.format("Name \"%s\" Already Exist In This Category",name));
    }
}

