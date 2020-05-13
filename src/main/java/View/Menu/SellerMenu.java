package View.Menu;

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
        subMenus.put("Product Menu" ,new SellerMenu(this));
        subMenus.put("Off Menu" , new SellerMenu(this));

        this.setSubMenus(subMenus);
        sellerMenu = this;
    }
}
