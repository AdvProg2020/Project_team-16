package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

import java.util.HashMap;

public class OFFSMenu extends Menu {
    private static OFFSMenu offsMenu;

    public static OFFSMenu getInstance() {
        return offsMenu;
    }

    public OFFSMenu(Menu parent){
        super("Offs Menu",parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Sort Menu",new SortMenu(this));
        subMenus.put("Filter Menu" ,new FilterMenu(this));

        this.setSubMenus(subMenus);
        offsMenu = this;
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("show product")){
            CommandProcessor.viewProduct(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        if (menuName.equalsIgnoreCase("sorting")){
            goToSubMenu("Sort Menu");
        }else if (menuName.equalsIgnoreCase("filtering")){
            goToSubMenu("Filter Menu");
        }else {
            throw new NotAnAvailableMenu();
        }
    }

    @Override
    void additionalPrints() {
        CommandProcessor.showAllOffs();
    }
}