package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class ManageUserMenu extends Menu {
    public ManageUserMenu(Menu parent) {
        super("Manage Users menu",parent);
    }

    @Override
    public void execute() {
        CommandProcessor.viewAllUsers();
        super.execute();
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("view")){
            CommandProcessor.viewUser(command);
        }else if (command.startsWith("delete user")){
            CommandProcessor.deleteUser(command);
        }else if (command.equalsIgnoreCase("create manager profile")){
            CommandProcessor.createManagerProfile();
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }
}
