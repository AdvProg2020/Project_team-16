package View.PrintModels;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class DisCodeManagerPM extends DiscountPM {
    private ArrayList<UserIntegerPM> users;

    public DisCodeManagerPM(String discountCode, Date startTime, Date endTime, int offPercentage, long maxOfPriceDiscounted, ArrayList<UserIntegerPM> users) {
        super(discountCode, startTime, endTime, offPercentage, maxOfPriceDiscounted);
        this.users = users;
    }

    @Override
    public String toString() {
        return super.getDiscountCode();
    }
}
