package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

import java.util.HashMap;

public class ProductSMenu extends Menu{
    private static ProductSMenu productSMenu;

    public static ProductSMenu getInstance() {
        return productSMenu;
    }

    ProductSMenu(Menu parent) {
        super("Products Menu",parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Sort Menu",new SortMenu(this));
        subMenus.put("Filter Menu" ,new FilterMenu(this));

        this.setSubMenus(subMenus);
        productSMenu = this;
    }

    @Override
    void helpPrinter() {
        /* TODO */
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("view categories")){
            CommandProcessor.showCategories();
        } else if (command.equalsIgnoreCase("show products")){
            CommandProcessor.showProductsInProductMenu();
        } else if (command.startsWith("show product")){
            CommandProcessor.viewProduct(command);
        } else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        if (menuName.equalsIgnoreCase("filtering")){
            goToSubMenu("Filter Menu");
        } else if (menuName.equalsIgnoreCase("sorting")){
            goToSubMenu("Sort Menu");
        }
    }
    @Override
    void additionalPrints() {

    }
}
