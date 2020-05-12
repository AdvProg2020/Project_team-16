package ModelPackage.Off;

import ModelPackage.Product.Product;
import ModelPackage.Users.Seller;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "t_off")
public class Off {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private int offId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SELLER")
    private Seller seller;

    @ElementCollection(targetClass = Product.class)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PRODUCTS")
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private OffStatus offStatus;

    @Temporal(TemporalType.DATE)
    private Date startTime;

    @Temporal(TemporalType.DATE)
    private Date endTime;

    @Column(name = "OFF_PERCENTAGE")
    private int offPercentage;

    public Off(){
        products = new ArrayList<>();
    }
}

