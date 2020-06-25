package View;
import lombok.Data;

import java.util.HashMap;

@Data
public class FilterPackage {
    private int upPriceLimit;
    private int downPriceLimit;
    private int categoryId;
    private HashMap<String,String> activeFilters;
    private boolean offMode;

    public FilterPackage(){
        activeFilters = new HashMap<>();
        offMode = false;
        upPriceLimit = 0;
        downPriceLimit = 0;
    }

    public FilterPackage(int categoryId) {
        activeFilters = new HashMap<>();
        offMode = false;
        upPriceLimit = 0;
        downPriceLimit = 0;
        this.categoryId = categoryId;
    }

    void reset(){
        activeFilters.clear();
        downPriceLimit = 0;
        upPriceLimit = 0;
    }

    public void disableAFilter(String filter){
        activeFilters.remove(filter);
    }
}
