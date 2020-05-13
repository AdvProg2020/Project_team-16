package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

import java.util.HashMap;

public class ManagerMenu extends Menu {
    private static ManagerMenu managerMenu;

    public static ManagerMenu getInstance() {
        return managerMenu;
    }

    public ManagerMenu(Menu parent) {
        super("Manager Menu", parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Personal Info Menu" ,new PersonalInfoMenu(this));
        subMenus.put("Manage Users Menu" ,new ManageUersMenu(this));
        subMenus.put("Product Menu" ,new ManagerProductMenu(this));
        subMenus.put("Discount Code Menu" ,new ManagerDiscountCodeMenu(this));
        subMenus.put("Request Menu" ,new ManagerRequestMenu(this));
        subMenus.put("Category Menu" ,new ManagerCategoryManager(this));

        this.setSubMenus(subMenus);
        ManagerMenu.managerMenu = this;
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.trim().equalsIgnoreCase("create discount code")){
            CommandProcessor.createDiscountCode(command);
        }
        else throw new InvalidCommandException();
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        if (menuName.equalsIgnoreCase("view personal info")){
            goToSubMenu("Personal Info Menu");
        }else if (menuName.equalsIgnoreCase("manage users")){
            goToSubMenu("Manage Users Menu");
        }else if (menuName.equalsIgnoreCase("manage all products")){
            goToSubMenu("Product Menu");
        }else if (menuName.equalsIgnoreCase("view discount codes")){
            goToSubMenu("Discount Code Menu");
        }else if (menuName.equalsIgnoreCase("manage requests")){
            goToSubMenu("Request Menu");
        }else if (menuName.equalsIgnoreCase("manage categories")){
            goToSubMenu("Category Menu");
        }else {
            throw new NotAnAvailableMenu();
        }
    }
}
