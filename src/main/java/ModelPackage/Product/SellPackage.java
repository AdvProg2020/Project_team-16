package ModelPackage.Product;
import ModelPackage.Off.Off;
import ModelPackage.Users.Seller;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;

import javax.persistence.*;

@Entity
@Table(name = "t_sell_package")
@Data @NoArgsConstructor
public class SellPackage {
    @Id@GeneratedValue
    int id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private Seller seller;
    @Column(name = "PRICE")
    private int price;
    @Column(name = "STOCK")
    private int stock;
    @ManyToOne
    private Off off;
    @Column(name = "IS_ON_OFF")
    private boolean isOnOff;
    @Column(name = "IS_AVAILABLE")
    private boolean isAvailable;

    public SellPackage(Product product, Seller seller, int price, int stock, Off off, boolean isOnOff, boolean isAvailable) {
        this.product = product;
        this.seller = seller;
        this.price = price;
        this.stock = stock;
        this.off = off;
        this.isOnOff = isOnOff;
        this.isAvailable = isAvailable;
    }
}
