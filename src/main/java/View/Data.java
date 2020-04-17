package View;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Builder @lombok.Data @NoArgsConstructor
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

enum SortType{
    BY_NAME,
    BY_VIEW,
    BY_PRICE,
    BY_BOUGHT_AMOUNT,
    BT_TIME;
}