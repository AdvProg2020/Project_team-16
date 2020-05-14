package View;

import View.Menu.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

@lombok.Data
public class Data {
    private String username;
    private String categoryInId;
    private String productSeeingInId;
    private String role;
    private Queue<Menu> menuHistory;

    /* Filter And Sort Options */
    private boolean isAscending;
    private SortType sortType;
    private int upPriceLimit;
    private int downPriceLimit;
    private ArrayList<String> publicFeatures;
    private ArrayList<String> specialFeatures;
    private HashMap<String,String> activeFilters;
    private boolean isOnOffMode;

    public void addMenuToHistory(Menu menu){
        menuHistory.add(menu);
    }

    public Menu dropLastMenu(){
        return menuHistory.poll();
    }
}