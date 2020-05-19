package View;
import lombok.Data;

import java.util.HashMap;

@Data
public class FilterPackage {
    private int upPriceLimit;
    private int downPriceLimit;
    private int categoryId;
    private HashMap<String,String> activeFilters;

    public FilterPackage(){
        activeFilters = new HashMap<>();
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
