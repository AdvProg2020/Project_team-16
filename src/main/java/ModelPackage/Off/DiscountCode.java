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
}
