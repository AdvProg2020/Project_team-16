package ModelPackage.System.exeption.discount;

public class StartingDateIsAfterEndingDate extends Exception {
    public StartingDateIsAfterEndingDate() {
        super("Your starting date is after ending date!");
    }
}
