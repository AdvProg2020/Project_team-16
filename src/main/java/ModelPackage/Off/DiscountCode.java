package ModelPackage.Off;


import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Users.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@Entity
@Table(name = "t_discountCode")
public class DiscountCode {
    @Id
    @Column(name = "CODE",unique = true)
    private String code;

    @Temporal(TemporalType.DATE)
    private Date startTime;

    @Temporal(TemporalType.DATE)
    private Date endTime;

    private int offPercentage;

    private long maxDiscount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERS")
    private List<UserIntegerMap> users;

    public DiscountCode(Date startTime, Date endTime, int offPercentage, long maxDiscount) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
        this.maxDiscount = maxDiscount;
        this.code = generateId();
        this.users = new ArrayList<>();
    }
    private static String generateId(){
        Date date = new Date();
        return String.format("DISCODE%s%04d",date.toString().replaceAll("\\s | ':'",""),(int)(Math.random()*9999+1));
    }
}
