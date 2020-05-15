package View.PrintModels;

import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.UserIntegerMap;
import lombok.Data;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
public class DisCodeManagerPM extends DiscountPM {
    private List<UserIntegerMap> usersHavingDiscountCodeWithCount;

    public DisCodeManagerPM(String discountCode, Date startTime, Date endTime, int offPercentage, long maxOfPriceDiscounted, List<UserIntegerMap> usersHavingDiscountCodeWithCount) {
        super(discountCode, startTime, endTime, offPercentage, maxOfPriceDiscounted);
        this.usersHavingDiscountCodeWithCount = usersHavingDiscountCodeWithCount;
    }
}
