package ModelPackage.System.exeption.account;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(long moneyNeeded) {
        super(String.format("You need %d more money. Please charge your wallet first.", moneyNeeded));
    }
}
