package View.Menu;

import java.util.HashMap;

public class ManagerMenu extends Menu {
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
    }
}
