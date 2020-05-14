package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

import java.util.HashMap;

public class CustomerMenu extends Menu {
    private static CustomerMenu customerMenu;

    public static CustomerMenu getInstance() {
        return customerMenu;
    }

    public CustomerMenu( Menu parent) {
        super("Customer Menu", parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Personal Info Menu",new PersonalInfoMenu(this));
        subMenus.put("Cart Menu",new CartMenu(this));
        subMenus.put("Purchase Menu",new PurchaseMenu(this));
        subMenus.put("Order Menu",new OrderMenu(this));

        this.setSubMenus(subMenus);
        CustomerMenu.customerMenu = this;
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("view balance")){
            CommandProcessor.viewBalanceCustomer();
        }else if (command.equalsIgnoreCase("view discount codes")){
            CommandProcessor.viewDiscountCodesCustomer();
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        if (menuName.equalsIgnoreCase("view personal info")){
            goToSubMenu("Personal Info Menu");
        }else if (menuName.equalsIgnoreCase("view cart")){
            goToSubMenu("Cart Menu");
        }else if (menuName.equalsIgnoreCase("view orders")){
            goToSubMenu("Order Menu");
        }else {
            throw new NotAnAvailableMenu();
        }
    }

    @Override
    void additionalPrints() {

    }
}
