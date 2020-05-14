package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class OrderMenu extends Menu {

    public OrderMenu(Menu parent) {
        super("Order Menu" , parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("show order")){
            CommandProcessor.showOrder(command);
        }else if (command.startsWith("rate")){
            CommandProcessor.rateOrder(command);
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
        CommandProcessor.showOrders();
    }
}
