package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

import java.util.HashMap;

public class SellerMenu extends Menu {
    private static SellerMenu sellerMenu;

    public static SellerMenu getInstance() {
        return sellerMenu;
    }

    public SellerMenu(Menu parent) {
        super("Seller Menu", parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Personal Info Menu" , new PersonalInfoMenu(this));
        subMenus.put("Product Menu" ,new SellerProductMenu(this));
        subMenus.put("Off Menu" , new SellerOffMenu(this));

        this.setSubMenus(subMenus);
        sellerMenu = this;
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("view company information")){
            CommandProcessor.viewCompanyInformation();
        }else if (command.equalsIgnoreCase("view sales history")){
            CommandProcessor.viewSalesHistory();
        }else if (command.equalsIgnoreCase("add product")){
            CommandProcessor.addProduct();
        }else if (command.equalsIgnoreCase("show categories")){
            CommandProcessor.showCategories();
        }else if (command.equalsIgnoreCase("view balance")){
            CommandProcessor.viewBalanceSeller();
        } else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        if (menuName.equalsIgnoreCase("view personal info")){
            goToSubMenu("Personal Info Menu");
        }else if (menuName.equalsIgnoreCase("manage products")){
            goToSubMenu("Product Menu");
        }else if (menuName.equalsIgnoreCase("view offs")){
            goToSubMenu("Off Menu");
        }else {
            throw new NotAnAvailableMenu();
        }
    }

    @Override
    void additionalPrints() {

    }
}
