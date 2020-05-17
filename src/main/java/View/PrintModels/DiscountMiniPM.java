package View.PrintModels;

import lombok.Data;

@Data
public class DiscountMiniPM {
    private String discountCode;
    private int offPercentage;

    public DiscountMiniPM(String discountCode, int offPercentage) {
        this.discountCode = discountCode;
        this.offPercentage = offPercentage;
    }
}
