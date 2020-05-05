package ModelPackage.Maps;

import ModelPackage.Off.DiscountCode;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity @Table(name = "t_discount_int_map")
public class DiscountcodeIntegerMap {
    @Id@GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "CODE")
    private DiscountCode discountCode;

    @Column(name = "INTEGER_VALUE")
    int integer;
}
