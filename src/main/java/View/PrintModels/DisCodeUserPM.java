package View.PrintModels;

import lombok.Data;

import java.sql.Time;

@Data
public class DisCodeUserPM extends DiscountPM {
    private int count;

    public DisCodeUserPM(String discountCode, Time startTime, Time endTime, int offPercentage, int maxOfPriceDiscounted, int count) {
        super(discountCode, startTime, endTime, offPercentage, maxOfPriceDiscounted);
        this.count = count;
    }
}
