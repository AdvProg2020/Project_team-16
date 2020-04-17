package View.PrintModels;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data @Builder
public class DisCodeManagerPM extends DiscountPM {
    private HashMap<String,Integer> usersHavingDiscountCodeWithCount;
}
