package View.Menu;

import java.util.HashMap;

public class CustomerMenu extends Menu {
    public CustomerMenu( Menu parent) {
        super("Customer Menu", parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Personal Info Menu",new PersonalInfoMenu(this));
        subMenus.put("Cart Menu",new CartMenu(this));
        subMenus.put("Purchase Menu",new PurchaseMenu(this));
        subMenus.put("Order Menu",new OrderMenu(this));

        this.setSubMenus(subMenus);
    }
}
