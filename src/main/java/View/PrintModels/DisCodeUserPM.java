package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data
public class DisCodeUserPM extends DiscountPM {
    private int count;

    public DisCodeUserPM(String discountCode, Date startTime, Date endTime, int offPercentage, long maxOfPriceDiscounted, int count) {
        super(discountCode, startTime, endTime, offPercentage, maxOfPriceDiscounted);
        this.count = count;
    }
}
