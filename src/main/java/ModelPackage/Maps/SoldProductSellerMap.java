package ModelPackage.Maps;

import ModelPackage.Product.SoldProduct;
import ModelPackage.Users.Seller;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_sp_s_map")
public class SoldProductSellerMap {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @OneToOne
        @JoinColumn(name =  "SOLD_PRODUCT")
    private SoldProduct soldProduct;

    @OneToOne
        @JoinColumn(name = "SELLER")
    private Seller seller;
}
