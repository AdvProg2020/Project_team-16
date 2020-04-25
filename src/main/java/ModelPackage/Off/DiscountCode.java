package ModelPackage.Off;


import ModelPackage.Users.User;
import lombok.*;

import java.util.Date;
import java.util.HashMap;

@Data
//@Builder
public class DiscountCode {
    private String code;
    private Date startTime;
    private Date endTime;
    private int offPercentage;
    private long maxDiscount;
    private HashMap<User,Integer> users;

    public DiscountCode(Date startTime, Date endTime, int offPercentage, long maxDiscount, HashMap<User, Integer> users) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
        this.maxDiscount = maxDiscount;
        this.users = users;
        this.code = generateId();
    }
    private static String generateId(){
        Date date = new Date();
        return String.format("DISCODE%s%04d",date.toString().replaceAll("\\s | ':'",""),(int)(Math.random()*9999+1));
    }
}
