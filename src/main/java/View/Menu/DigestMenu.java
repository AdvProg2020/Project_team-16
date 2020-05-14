package View.Menu;

import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class DigestMenu extends Menu {
    public DigestMenu(Menu parent) {
        super("Digest Menu",parent);
    }

    @Override
    void helpPrinter() {

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
