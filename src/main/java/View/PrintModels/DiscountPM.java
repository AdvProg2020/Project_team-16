package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data
public abstract class DiscountPM {
    private String discountCode;
    private Date startTime;
    private Date endTime;
    private int offPercentage;
    private long maxOfPriceDiscounted;

    public DiscountPM(String discountCode, Date startTime, Date endTime, int offPercentage, long maxOfPriceDiscounted) {
        this.discountCode = discountCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
        this.maxOfPriceDiscounted = maxOfPriceDiscounted;
    }
}
