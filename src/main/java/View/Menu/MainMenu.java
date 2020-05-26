package View.Menu;

import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
public class MainMenu extends Menu {
    private static MainMenu mainMenu;

    public static MainMenu getInstance() {
        return mainMenu;
    }

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
        mainMenu = this;
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        throw new InvalidCommandException();
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }

    @Override
    void additionalPrints() {

    }
}
