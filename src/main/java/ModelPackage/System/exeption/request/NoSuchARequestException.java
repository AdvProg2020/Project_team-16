package ModelPackage.System.exeption.request;

public class NoSuchARequestException extends Exception {
    public NoSuchARequestException(int id){
        super(String.format("There Isn't Any Request With Id (%d)",id));
    }
}
