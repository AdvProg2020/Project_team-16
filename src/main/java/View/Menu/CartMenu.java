package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class CartMenu extends Menu{
    public CartMenu(Menu parent) {
        super("Cart Menu",parent);
    }


    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("show products")){
            CommandProcessor.showProductsInCart();
        }else if (command.startsWith("view")){
            CommandProcessor.viewProduct(command);
        }else if (command.startsWith("increase")){
            CommandProcessor.increseProductInCart(command);
        }else if (command.startsWith("decrease")){
            CommandProcessor.decreaseProductInCart(command);
        }else if (command.equalsIgnoreCase("show total price")){
            CommandProcessor.showTotalPrice();
        }else if (command.equalsIgnoreCase("purchase")){
            CustomerMenu.getInstance().goToSubMenu("Purchase Menu");
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
        CommandProcessor.showCart();
    }
}
