package ModelPackage.Off;


import ModelPackage.Users.User;
import lombok.*;

import java.sql.Time;
import java.util.HashMap;

@Data
@Builder
public class DiscountCode {
    private String code;
    private Time startTime;
    private Time endTime;
    private int offPercentage;
    private long maxDiscount;
    private HashMap<User,Integer> users;

    public String codeGenerator(){}
}
