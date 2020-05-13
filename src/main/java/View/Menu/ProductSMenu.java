package View.Menu;

import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;
import View.exceptions.NotSignedInYetException;

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

    }

    @Override
    protected void goToMenuIfAvailable(String menuName) throws NotSignedInYetException {
        try {
            super.goToMenuIfAvailable(menuName);
        } catch (NotSignedInYetException e) {
           throw new NotSignedInYetException();
        } catch (NotAnAvailableMenu ee) {

        }
    }

    private void goToSubMenusIfAvailable(String menuName){

    }
}
