package ModelPackage.Maps;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Users.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity @Table(name = "t_user_int_map")
public class UserIntegerMap {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER")
    private User user;

    @Column(name = "INTEGER_VALUE")
    int integer;

    @ManyToOne
    private DiscountCode discountCode;
}
