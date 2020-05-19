package View;

import ModelPackage.Users.Cart;
import View.Menu.Menu;
import dnl.utils.text.table.TextTable;

import java.util.*;

@lombok.Data
public class Data {
    private static Data data = new Data();
    public static Data getInstance() {
        return data;
    }

    private String username;
    private int categoryInId;
    private int productSeeingInId;
    private String role;
    private Queue<Menu> menuHistory;
    private NotSignedInCart cart;

    /* Filter And Sort Options */
    private FilterPackage filters;
    private SortPackage sorts;
    private boolean isOnOffMode;
    private List<String> publicFeatures;
    private List<String> specialFeatures;



    public void addMenuToHistory(Menu menu){
        menuHistory.add(menu);
    }

    public Menu dropLastMenu(){
        return menuHistory.poll();
    }

    public Menu getLastMenu(){return menuHistory.peek();}

    private Data(){
        cart = new NotSignedInCart();
        filters = new FilterPackage();
        sorts = new SortPackage();
        menuHistory = new LinkedList<>();
        publicFeatures = new ArrayList<>();
        specialFeatures = new ArrayList<>();
    }

    public void logout(){
        cart.clear();
    }

    public void reset(){
        sorts.reset();
        filters.reset();
    }

    public void printFilters(){
        String[] column = {"Filter","Data"};
        HashMap<String,String> map = filters.getActiveFilters();
        Object[][] data = new Object[map.size()][2];
        int i =0;
        for (String key : map.keySet()) {
            data[i][0] = key;
            data[i][1] = map.get(key);
            i++;
        }

        TextTable textTable = new TextTable(column,data);
        textTable.setSort(0);
        textTable.printTable();
    }

    public void allPrintFilters() {

    }
}