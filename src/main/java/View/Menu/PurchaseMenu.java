package View.Menu;

import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class PurchaseMenu extends Menu {
    public PurchaseMenu(Menu parent) {
        super("Purchase Menu",parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        /*TODO*/
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        /*TODO*/
    }

    @Override
    void additionalPrints() {
        /*TODO*/
    }
}
