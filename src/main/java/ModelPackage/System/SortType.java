package ModelPackage.System;

public enum SortType {
    NAME,
    VIEW,
    MORE_PRICE,
    LESS_PRICE,
    BOUGHT_AMOUNT,
    TIME,
    SCORE,
    CATEGORIZED_REQUESTS,
    DEFAULT;

    public static SortType valueOF(String string) {
        switch (string) {
            case "Price":
                return MORE_PRICE;
            case "Date Added":
                return TIME;
            case "View":
                return VIEW;
            case "Bought":
                return BOUGHT_AMOUNT;
            case "Name":
                return NAME;
            case "Score":
                return SCORE;
            default:
                return DEFAULT;
        }
    }
}
