package ModelPackage.Log;

import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Product.SoldProduct;
import ModelPackage.Users.Seller;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Setter @Getter
@Entity
@Table(name = "t_purchase_log")
public class PurchaseLog extends Log {
    @ElementCollection(targetClass = SoldProductSellerMap.class)
        @OneToMany(cascade = CascadeType.ALL)
        @JoinColumn(name = "PRODUCTS")
    private List<SoldProductSellerMap> productsAndItsSellers;

    @Column(name = "PRICE_PAID")
    private int pricePaid;

    @Column(name = "DISCOUNT")
    private int discount;

    public PurchaseLog(Date date, DeliveryStatus deliveryStatus, List<SoldProductSellerMap> productsAndItsSellers, int pricePaid, int discount) {
        super(date, deliveryStatus);
        this.productsAndItsSellers = productsAndItsSellers;
        this.pricePaid = pricePaid;
        this.discount = discount;
        //this.sellers = sellers;
    }

    public PurchaseLog(){
        this.productsAndItsSellers = new ArrayList<>();
    }

    public boolean containsProduct(int id){
        for (SoldProductSellerMap map : productsAndItsSellers) {
            if (map.isProduct(id)) return true;
        }
        return false;
    }
}