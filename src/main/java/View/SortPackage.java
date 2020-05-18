package View;

import ModelPackage.System.SortType;
import lombok.Data;

@Data
public class SortPackage {
    private boolean isAscending;
    private SortType sortType;

    void reset(){
        sortType = SortType.VIEW;
        isAscending = true;
    }
}
