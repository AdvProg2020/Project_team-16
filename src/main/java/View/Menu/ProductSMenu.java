package View.Menu;

import java.util.HashMap;

public class ProductSMenu extends Menu{
    private static ProductSMenu productSMenu;

    public static ProductSMenu getInstance() {
        return productSMenu;
    }

    public ProductSMenu(Menu parent) {
        super("Products Menu",parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Sort Menu",new SortMenu(this));
        subMenus.put("Filter Menu" ,new FilterMenu(this));

        this.setSubMenus(subMenus);
        productSMenu = this;
    }
}
