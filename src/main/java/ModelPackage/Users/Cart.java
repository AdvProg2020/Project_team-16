package ModelPackage.Users;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data @Entity
@Table(name = "t_cart")
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ElementCollection(targetClass = SubCart.class)
    @OneToMany
        @Column(name = "SUB_CARTS")
    private List<SubCart> subCarts;

    @Column(name = "TOTAL_PRICE")
    private long totalPrice;

    public Cart() {
        subCarts = new ArrayList<>();
    }
}
