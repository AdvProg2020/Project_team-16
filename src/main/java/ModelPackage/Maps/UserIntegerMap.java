package ModelPackage.Maps;

import ModelPackage.Users.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity @Table(name = "t_user_int_map")
public class UserIntegerMap {
    @Id @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "USER")
    private User user;

    @Column(name = "INTEGER_VALUE")
    int integer;
}
