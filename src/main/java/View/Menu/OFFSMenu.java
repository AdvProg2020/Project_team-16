package View.Menu;

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
}