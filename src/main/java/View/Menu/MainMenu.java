package View.Menu;

import lombok.Data;

import java.util.HashMap;

@Data
public class MainMenu extends Menu {
    public MainMenu(){
        super("Main Menu",null);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Manager Menu",new ManagerMenu(this));
        subMenus.put("Account Menu",new AccountMenu(this));
        subMenus.put("Seller Menu",new SellerMenu(this));
        subMenus.put("Customer Menu",new CustomerMenu(this));
        subMenus.put("Product Menu",new ProductMenu(this));
        subMenus.put("Products Menu",new ProductSMenu(this));
        subMenus.put("Off Menu",new OFFSMenu(this));

        this.setSubMenus(subMenus);
    }
}
