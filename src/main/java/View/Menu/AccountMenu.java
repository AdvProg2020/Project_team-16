package View.Menu;

import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;
import View.exceptions.NotSignedInYetException;

public class AccountMenu extends Menu {
    private static AccountMenu accountMenu;
    public AccountMenu(Menu parent){
        super("Account Menu",parent);
        AccountMenu.accountMenu = this;
    }
    public static AccountMenu getInstance() {
        return accountMenu;
    }

    public void goToMenuIfAvailable(String input) throws NotAnAvailableMenu, NotSignedInYetException {
        super.goToMenuIfAvailable(input);
    }

    @Override
    void helpPrinter() {

    }

    @Override
    void processCommand(String command) throws InvalidCommandException {

    }
}
