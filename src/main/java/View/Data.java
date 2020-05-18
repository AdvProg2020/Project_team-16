package View;

import View.Menu.Menu;

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
        filters = new FilterPackage();
        sorts = new SortPackage();
        menuHistory = new LinkedList<>();
        publicFeatures = new ArrayList<>();
        specialFeatures = new ArrayList<>();
    }

    public void logout(){

    }

    public void reset(){
        sorts.reset();
        filters.reset();
    }
}