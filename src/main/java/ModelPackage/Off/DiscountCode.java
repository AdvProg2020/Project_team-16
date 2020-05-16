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

    public DiscountCode(String code,Date startTime, Date endTime, int offPercentage, long maxDiscount) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
        this.maxDiscount = maxDiscount;
        this.code = code;
        this.users = new ArrayList<>();
    }

}
