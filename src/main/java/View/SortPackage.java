package View;

import ModelPackage.System.SortType;
import lombok.Data;

@Data
public class SortPackage {
    private boolean isAscending;
    private SortType sortType;

    public void reset(){
        sortType = SortType.DEAFAULT;
        isAscending = true;
    }
}
