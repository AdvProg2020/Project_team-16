package ModelPackage.System.exeption.discount;

public class NotValidPercentageException extends Exception {
    public NotValidPercentageException(int percentage) {
        super(String.format("Your percentage (%d) is not between 0 and 100", percentage));
    }
}
