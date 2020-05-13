package View;

import java.util.ArrayList;
import java.util.HashMap;

@lombok.Data
public class Data {
    private String username;
    private String categoryInId;
    private String productSeeingInId;
    private String role;

    /* Filter And Sort Options */
    private boolean isAscending;
    private SortType sortType;
    private int upPriceLimit;
    private int downPriceLimit;
    private ArrayList<String> publicFeatures;
    private ArrayList<String> specialFeatures;
    private HashMap<String,String> activeFilters;
    private boolean isOnOffMode;
}