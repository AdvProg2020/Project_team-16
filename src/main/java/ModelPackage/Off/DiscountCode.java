package ModelPackage.Off;


import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Users.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Entity
@Table(name = "t_discount_code")
public class DiscountCode {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME")
    private Date startTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "OFF_PERCENTAGE")
    private int offPercentage;

    @Column(name = "MAX_DISCOUNT")
    private long maxDiscount;

    @ElementCollection(targetClass = UserIntegerMap.class)
        @OneToMany
    private List<UserIntegerMap> usersHaveThisCodeAndAmount;
}
