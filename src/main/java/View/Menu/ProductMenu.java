package View.Menu;

import java.util.HashMap;

public class ProductMenu extends Menu {
    public ProductMenu( Menu parent) {
        super("Product Menu", parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Digest Menu", new DigestMenu(this));

        this.setSubMenus(subMenus);
    }
}
