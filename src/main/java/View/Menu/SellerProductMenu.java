package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class SellerProductMenu extends Menu{
    public SellerProductMenu(Menu parent) {
        super("Product Menu" , parent);
    }

    @Override
    public void execute() {
        CommandProcessor.viewAllProductSeller();
        super.execute();
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("view")){
            CommandProcessor.viewProduct(command);
        }else if (command.startsWith("edit")){
            CommandProcessor.editProduct(command);
        }else if (command.startsWith("view buyers")){
            CommandProcessor.viewBuyersOfThisProduct(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }
}
