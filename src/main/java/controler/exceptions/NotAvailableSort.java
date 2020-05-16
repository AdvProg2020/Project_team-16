package controler.exceptions;

public class NotAvailableSort extends Exception {
    public NotAvailableSort(String sortType) {
        super(String.format("Your requested sort (%s) is not available!", sortType));
    }
}
