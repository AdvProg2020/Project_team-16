package View.Menu;

import View.CommandProcessor;
import View.Data;
import View.Printer;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class DigestMenu extends Menu {
    public DigestMenu(Menu parent) {
        super("Digest Menu",parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("add to cart")){
            if (Data.getInstance().getRole().isEmpty())
                Data.getInstance().getCart().addToCart();
            else
                CommandProcessor.addToCart();
        }else if (command.startsWith("select seller")){
            if (Data.getInstance().getRole().isEmpty())
                Printer.printMessage("You only can set seller in cart only if you logged in");
            else
                CommandProcessor.selectSeller(command);
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
        CommandProcessor.briefInfo();
    }
}
