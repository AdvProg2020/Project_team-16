package View.PrintModels;

import lombok.Data;

import java.sql.Time;
import java.util.HashMap;

@Data
public class DisCodeManagerPM extends DiscountPM {
    private HashMap<String,Integer> usersHavingDiscountCodeWithCount;

    public DisCodeManagerPM(String discountCode, Time startTime, Time endTime, int offPercentage, int maxOfPriceDiscounted, HashMap<String, Integer> usersHavingDiscountCodeWithCount) {
        super(discountCode, startTime, endTime, offPercentage, maxOfPriceDiscounted);
        this.usersHavingDiscountCodeWithCount = usersHavingDiscountCodeWithCount;
    }
}
