package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

import java.util.HashMap;

public class ProductMenu extends Menu {

    public ProductMenu( Menu parent) {
        super("Product Menu", parent);
        HashMap<String,Menu> subMenus = new HashMap<String, Menu>();
        subMenus.put("Digest Menu", new DigestMenu(this));
        subMenus.put("CommentMenu",new CommentMenu(this));
        this.setSubMenus(subMenus);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("attributes")){
            CommandProcessor.attributes();
        }else if (command.startsWith("compare")){
            CommandProcessor.compare(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        if (menuName.equalsIgnoreCase("digest")){
            goToSubMenu("Digest Menu");
        }else if (menuName.equalsIgnoreCase("comments")){
            goToSubMenu("CommentMenu");
        }else {
            throw new NotAnAvailableMenu();
        }
    }

    @Override
    void additionalPrints() {

    }
}
