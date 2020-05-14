package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class FilterMenu extends Menu {
    public FilterMenu(Menu parent) {
        super("Filter Menu" , parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("show available filters")){
            CommandProcessor.showAvailableFilters();
        }else if (command.startsWith("filter")){
            CommandProcessor.filter(command);
        }else if (command.equalsIgnoreCase("current filters")){
            CommandProcessor.curruntFilters();
        }else if (command.startsWith("disable a filter")){
            CommandProcessor.disableAFilter(command);
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
        CommandProcessor.updateFilters();
    }
}
