package ModelPackage.Users;

import ModelPackage.Product.Product;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_sub_carts")
public class SubCart{
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @OneToOne
        @JoinColumn(name = "PRODUCT")
    private Product product;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "SELLER")
    private Seller seller;

    @Column(name = "AMOUNT")
    private int amount;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cart cart;

    public SubCart(Product product, Seller seller, int amount) {
        this.product = product;
        this.seller = seller;
        this.amount = amount;
    }

    public SubCart(){

    }
}
