package View;

import ModelPackage.System.SortType;
import lombok.Data;

import java.util.HashMap;

@Data
public class SortAndFilterPackage {
    private boolean isAscending;
    private SortType sortType;
    private int upPriceLimit;
    private int downPriceLimit;
    private HashMap<String,String> activeFilters;

    public SortAndFilterPackage(){

    }
}
