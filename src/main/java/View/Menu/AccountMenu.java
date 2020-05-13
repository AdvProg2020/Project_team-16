package View.Menu;

import View.CommandProcessor;
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
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("create account")){
            CommandProcessor.createAccount(command);
        }else if (command.startsWith("login")){
            CommandProcessor.login(command);
        }else{
            throw new InvalidCommandException();
        }
    }
}
