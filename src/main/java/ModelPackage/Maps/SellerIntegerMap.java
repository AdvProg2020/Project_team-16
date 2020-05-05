package ModelPackage.Maps;

import ModelPackage.Users.Seller;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_map")
public class SellerIntegerMap{
    @Id @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "SELLER")
    private Seller seller;
    @Column(name = "INTEGER_VALUE")
    private Integer integer;
}
