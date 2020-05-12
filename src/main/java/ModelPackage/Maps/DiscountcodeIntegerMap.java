package ModelPackage.Maps;

import ModelPackage.Off.DiscountCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity @Table(name = "t_discount_int_map")
public class DiscountcodeIntegerMap {
    @Setter(AccessLevel.NONE)
    @Id@GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CODE")
    private DiscountCode discountCode;

    @Column(name = "INTEGER_VALUE")
    int integer;
}
