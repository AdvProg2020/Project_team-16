package View.Menu;

import View.Data;
import View.Printer;
import View.Scan;
import View.exceptions.InvalidCommandException;
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
        this.additionalPrints();
        Scan scan = Scan.getInstance();
        while (true){
            String command = scan.getLine().trim();
            try {
                executeValidCommand(command);
            } catch (InvalidCommandException e) {
                try {
                    goToMenuIfAvailable(command);
                    break;
                } catch (NotAnAvailableMenu notAnAvailableMenu) {
                    Printer.printMessage("Invalid Command! Enter a valid one : ");
                } catch (NotSignedInYetException ex) {
                    Printer.printMessage(ex.getMessage());
                }
            }
        }
    }

    private void show(){
        Printer.clearScreen();
        Printer.MenuPrinter(this.name);
    }

    protected void goToMenuIfAvailable(String menuName) throws NotAnAvailableMenu, NotSignedInYetException {
        Data data = Data.getInstance();
        if (menuName.equalsIgnoreCase("help")) this.helpPrinter();
        else if(menuName.equalsIgnoreCase("login")
                ||menuName.equalsIgnoreCase("create account")) {
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
        }else if (menuName.equalsIgnoreCase("logout")){
            data.logout();
            this.execute();
        } else{
            goToSubMenusIfAvailable(menuName);
        }
    }

    abstract void helpPrinter();

    abstract void executeValidCommand(String command) throws InvalidCommandException;

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

    abstract void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu;

    void goToSubMenu(String submenu){
        Data data = Data.getInstance();
        Menu next = this.subMenus.get(submenu);
        data.addMenuToHistory(next);
        next.execute();
    }

    abstract void additionalPrints();
}
