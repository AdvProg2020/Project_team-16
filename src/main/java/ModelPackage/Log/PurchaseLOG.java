package ModelPackage.Log;

import ModelPackage.Product.Product;
import ModelPackage.Users.Seller;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;

@Setter @Getter
public class PurchaseLOG extends LOG {
    private HashMap<Product,Integer> productsAndItsPrices;
    private int pricePaid;
    private int discount;
    private HashMap<Seller,Integer> sellers;

    public PurchaseLOG(Date date, DeliveryStatus deliveryStatus, HashMap<Product, Integer> productsAndItsPrices, int pricePaid, int discount, HashMap<Seller, Integer> sellers) {
        super(date, deliveryStatus);
        this.productsAndItsPrices = productsAndItsPrices;
        this.pricePaid = pricePaid;
        this.discount = discount;
        this.sellers = sellers;
    }
}
