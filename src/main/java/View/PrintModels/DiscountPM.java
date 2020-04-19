package View.PrintModels;

import lombok.Data;

import java.sql.Time;

@Data
public abstract class DiscountPM {
    private String discountCode;
    private Time startTime;
    private Time endTime;
    private int offPercentage;
    private int maxOfPriceDiscounted;

    public DiscountPM(String discountCode, Time startTime, Time endTime, int offPercentage, int maxOfPriceDiscounted) {
        this.discountCode = discountCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
        this.maxOfPriceDiscounted = maxOfPriceDiscounted;
    }
}
