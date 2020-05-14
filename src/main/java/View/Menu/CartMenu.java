package View.Menu;

import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class CartMenu extends Menu{
    public CartMenu(Menu parent) {
        super("Cart Menu",parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {

    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {

    }

    @Override
    void additionalPrints() {

    }
}
