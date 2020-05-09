package ModelPackage.Users;

import ModelPackage.Product.Product;
import lombok.*;

import javax.persistence.*;

@Data @NoArgsConstructor
@Entity
@Table(name = "t_sub_carts")
public class SubCart{
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "PRODUCT")
    private Product product;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "SELLER")
    private Seller seller;

    @Column(name = "AMOUNT")
    private int amount;

    public SubCart(Product product, int productId, Seller seller, int amount) {
        this.product = product;
        this.id = productId;
        this.seller = seller;
        this.amount = amount;
    }
}
