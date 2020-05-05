package ModelPackage.Product;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_sold_products")
public class SoldProduct {
    @Id @GeneratedValue
    private int id;

    @Column(name = "SOURCE_ID")
    private int sourceId;

    @Column(name = "SOLD_PRICE")
    private int soldPrice;
}
