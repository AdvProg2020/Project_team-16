package View.PrintModels;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;

@Data @Builder
public abstract class DiscountPM {
    private String discountCode;
    private Time startTime;
    private Time endTime;
    private int offPercentage;
    private int maxOfPriceDiscounted;
}
