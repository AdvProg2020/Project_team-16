package View.Menu;

import View.Data;
import View.Printer;
import View.exceptions.NotAnAvailableMenu;
import View.exceptions.NotSignedInYetException;

import java.util.HashMap;

@lombok.Data
public abstract class Menu {
    private String name;
    private Menu parent;
    private HashMap<String,Menu> subMenus;

    public Menu(String name, Menu parent) {
        this.name = name;
        this.parent = parent;
    }

    public void execute(){
        this.show();

    }

    private void show(){
        Printer.clearScreen();
        Printer.MenuPrinter(this.name);
    }

    protected void goToMenuIfAvailable(String menuName) throws NotAnAvailableMenu, NotSignedInYetException {
        Data data = Data.getInstance();
        if (menuName.equalsIgnoreCase("help")) this.helpPrinter();
        else if(menuName.equalsIgnoreCase("login")
                ||menuName.equalsIgnoreCase("create account")
                ||menuName.equalsIgnoreCase("logout")) {
            data.addMenuToHistory(AccountMenu.getInstance());
            AccountMenu.getInstance().execute();
        }else if (menuName.equalsIgnoreCase("main menu")){
            mainMenuExecutor(data);
        }else if (menuName.equalsIgnoreCase("products menu")){
            data.addMenuToHistory(ProductSMenu.getInstance());
            ProductSMenu.getInstance().execute();
        }else if (menuName.equalsIgnoreCase("off menu")){
            data.addMenuToHistory(OFFSMenu.getInstance());
            OFFSMenu.getInstance().execute();
        }else if (menuName.equalsIgnoreCase("back")){
            Menu previous = data.dropLastMenu();
            previous.execute();
        }else if (menuName.equalsIgnoreCase("exit")){
            System.exit(1);
        }else{
            throw new NotAnAvailableMenu();
        }
    }

    abstract void helpPrinter();

    private static void mainMenuExecutor(Data data) throws NotSignedInYetException {
        switch (data.getRole()){
            case "Manager" :
                data.addMenuToHistory(ManagerMenu.getInstance());
                ManagerMenu.getInstance().execute();
                break;
            case "Customer" :
                data.addMenuToHistory(CustomerMenu.getInstance());
                CustomerMenu.getInstance().execute();
                break;
            case "Seller" :
                data.addMenuToHistory(SellerMenu.getInstance());
                SellerMenu.getInstance().execute();
                break;
            default:
                throw new NotSignedInYetException();
        }
    }
}
