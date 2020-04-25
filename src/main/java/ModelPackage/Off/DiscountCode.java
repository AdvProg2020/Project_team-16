package ModelPackage.Off;


import ModelPackage.Users.User;
import lombok.*;

import java.util.Date;
import java.util.HashMap;

@Data
@Builder
public class DiscountCode {
    private String code;
    private Date startTime;
    private Date endTime;
    private int offPercentage;
    private long maxDiscount;
    private HashMap<User,Integer> users;

    public DiscountCode(String code, Date startTime, Date endTime, int offPercentage, long maxDiscount, HashMap<User, Integer> users) {
        this.code = code;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
        this.maxDiscount = maxDiscount;
        this.users = users;
    }
}
