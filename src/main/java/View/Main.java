package View;

import ModelPackage.System.database.DBManager;
import View.Menu.MainMenu;

public class Main {
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        DBManager.initialLoad();
        mainMenu.execute();
    }
}
