package ModelPackage.System.exeption.category;

public class RepeatedFeatureException extends Exception {
    public RepeatedFeatureException(String feature,String categoryId){
        super(String.format("Feature (%s) Already Exists in Category With Id (%s)",feature,categoryId));
    }
}
