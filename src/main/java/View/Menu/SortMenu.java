package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class SortMenu extends Menu {
    public SortMenu(Menu parent) {
        super("Sort Menu",parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("show available sorts")){
            CommandProcessor.showAvailableFilters();
        }else if (command.equalsIgnoreCase("current sort")){
            CommandProcessor.currentSort();
        }else if (command.equalsIgnoreCase("disable sort")){
            CommandProcessor.disableSort();
        }else if (command.startsWith("sort")){
            CommandProcessor.sort(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }

    @Override
    void additionalPrints() {

    }
}
