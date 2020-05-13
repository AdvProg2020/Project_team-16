package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class ManagerCategoryManager extends Menu {
    public ManagerCategoryManager(Menu parent) {
        super("Category Menu",parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("add")){
            CommandProcessor.addCategory(command);
        }else if (command.startsWith("edit")){
            CommandProcessor.editCategory(command);
        }else if (command.startsWith("remove")){
            CommandProcessor.removeCategory(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }
}
