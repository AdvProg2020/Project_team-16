package ModelPackage.Users;

import ModelPackage.Product.Product;
import lombok.*;

import javax.persistence.*;

@Data @NoArgsConstructor
@Entity
@Table(name = "t_sub_carts")
public class SubCart{
    @Id @GeneratedValue
    private int id;

    @OneToOne
        @JoinColumn(name = "PRODUCT")
    private Product product;

    @OneToOne
        @JoinColumn(name = "SELLER")
    private Seller seller;

    @Column(name = "AMOUNT")
    private int amount;
}
