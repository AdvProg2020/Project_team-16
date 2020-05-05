package ModelPackage.Maps;

import ModelPackage.Product.SoldProduct;
import ModelPackage.Users.Seller;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_sp_s_map")
public class SoldProductSellerMap {
    @Id @GeneratedValue
    private int id;

    @OneToOne
        @JoinColumn(name =  "SOLD_PRODUCT")
    private SoldProduct soldProduct;

    @OneToOne
        @JoinColumn(name = "SELLER")
    private Seller seller;
}
