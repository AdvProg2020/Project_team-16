package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class ManagerDiscountCodeMenu extends Menu {
    public ManagerDiscountCodeMenu(Menu parent) {
        super("Discount Code Menu" , parent);
    }

    @Override
    void additionalPrints() {
        CommandProcessor.viewDiscountCodes();
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("view discount code")){
            CommandProcessor.viewDiscountCode(command);
        }else if (command.startsWith("edit discount code")){
            CommandProcessor.editDiscountCode(command);
        }else if (command.startsWith("remove discount code")){
            CommandProcessor.removeDiscountCode(command);
        }else if (command.startsWith("add user")){
            CommandProcessor.addUserToDiscountCode(command);
        }
        else if (command.startsWith("remove user")){
            CommandProcessor.removeUserFromDiscountCode(command);
        }
        else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }
}
