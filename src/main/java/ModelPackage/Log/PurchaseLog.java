package ModelPackage.Log;

import ModelPackage.Product.Product;
import ModelPackage.Users.Seller;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;

@Setter @Getter
public class PurchaseLog extends Log {
    private HashMap<String,String> productsAndItsSellers;
    private int pricePaid;
    private int discount;
    //private HashMap<String,String> sellers;

    public PurchaseLog(Date date, DeliveryStatus deliveryStatus, HashMap<String, String> productsAndItsSellers, int pricePaid, int discount) {
        super(date, deliveryStatus);
        this.productsAndItsSellers = productsAndItsSellers;
        this.pricePaid = pricePaid;
        this.discount = discount;
        //this.sellers = sellers;
    }
}
