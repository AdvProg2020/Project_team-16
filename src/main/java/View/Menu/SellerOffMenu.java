package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class SellerOffMenu extends Menu {
    public SellerOffMenu(Menu parent) {
        super("Off Menu",parent);
    }

    @Override
    void additionalPrints() {
        CommandProcessor.viewAllOffs();
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("view")){
            CommandProcessor.viewOff(command);
        }else if (command.startsWith("edit")){
            CommandProcessor.editOff(command);
        }else if (command.equalsIgnoreCase("add off")){
            CommandProcessor.addOff();
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }
}
