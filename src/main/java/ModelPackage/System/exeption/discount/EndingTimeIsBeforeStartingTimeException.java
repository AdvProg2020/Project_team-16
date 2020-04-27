package ModelPackage.System.exeption.discount;

public class EndingTimeIsBeforeStartingTimeException extends Exception{
    public EndingTimeIsBeforeStartingTimeException() {
        super("Your ending time is before starting time");
    }
}
