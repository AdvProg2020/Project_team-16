package ModelPackage.System.exeption.clcsmanager;

public class NoSuchACompanyException extends RuntimeException {
    public NoSuchACompanyException(int id){
        super(String.format("There Isn't Any Company With Id (%d)",id));
    }
}
