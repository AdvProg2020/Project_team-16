package View;

import ModelPackage.System.SortType;
import View.Menu.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

@lombok.Data
public class Data {
    private static Data data;
    public static Data getInstance() {
        return data;
    }

    private String username;
    private int categoryInId;
    private int productSeeingInId;
    private String role;
    private Queue<Menu> menuHistory;

    /* Filter And Sort Options */
    private SortAndFilterPackage sortAndFilters;
    private boolean isOnOffMode;
    private ArrayList<String> publicFeatures;
    private ArrayList<String> specialFeatures;

    public void addMenuToHistory(Menu menu){
        menuHistory.add(menu);
    }

    public Menu dropLastMenu(){
        return menuHistory.poll();
    }

    public Menu getLastMenu(){return menuHistory.peek();}

    private Data(){
        menuHistory = new LinkedList<>();
        publicFeatures = new ArrayList<>();
        specialFeatures = new ArrayList<>();
    }

    public void logout(){

    }
}