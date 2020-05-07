package ModelPackage.Maps;

import ModelPackage.Users.Seller;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_map")
public class SellerIntegerMap{
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "SELLER")
    private Seller seller;
    @Column(name = "INTEGER_VALUE")
    private Integer integer;
}
