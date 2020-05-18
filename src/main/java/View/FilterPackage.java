package View;
import lombok.Data;

import java.util.HashMap;

@Data
public class FilterPackage {
    private int upPriceLimit;
    private int downPriceLimit;
    private HashMap<String,String> activeFilters;

    public FilterPackage(){
        activeFilters = new HashMap<>();
    }

    void reset(){
        activeFilters.clear();
        downPriceLimit = 0;
        upPriceLimit = 0;
    }
}
