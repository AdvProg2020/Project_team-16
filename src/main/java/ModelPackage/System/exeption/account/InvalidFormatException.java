package ModelPackage.System.exeption.account;

public class InvalidFormatException extends Exception {
    public InvalidFormatException(String input, boolean isUsername) {
        super(String.format("%s '%s' is invalid. %ss has to be at least 8 characters long.",
                isUsername ? "Username" : "Password",
                input,
                isUsername ? "Username" : "Password"
        ));
    }
}
