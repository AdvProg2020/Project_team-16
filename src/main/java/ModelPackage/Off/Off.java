package ModelPackage.Off;

import ModelPackage.Product.Product;
import ModelPackage.Users.Seller;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
// TODO: 6/23/2020 Keep SellPackages On Off Instead of Products

@Data
@Entity
@Table(name = "t_off")
public class Off {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private int offId;

    @OneToOne
    @JoinColumn(name = "SELLER")
    private Seller seller;

    @ElementCollection(targetClass = Product.class)
    @OneToMany
    @JoinColumn(name = "PRODUCTS")
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private OffStatus offStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "OFF_PERCENTAGE")
    private int offPercentage;

    public Off(){
        products = new ArrayList<>();
    }
}

