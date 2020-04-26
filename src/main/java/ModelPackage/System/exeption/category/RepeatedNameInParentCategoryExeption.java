package ModelPackage.System.exeption.category;

public class RepeatedNameInParentCategoryExeption extends Exception {
    public RepeatedNameInParentCategoryExeption(String name){
        super(String.format("Name \"%s\" Already Exist In This Category",name));
    }
}

