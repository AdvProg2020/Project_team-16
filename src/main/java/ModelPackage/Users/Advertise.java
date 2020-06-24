package ModelPackage.Users;

import ModelPackage.Product.Product;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Advertise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private Product product;
    @Column(unique = true)
    private String username;
    private boolean active;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Advertise(Product product, String username) {
        this.product = product;
        this.username = username;
        active = false;
    }

    public Advertise() {
    }
}
