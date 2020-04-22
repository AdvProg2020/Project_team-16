package ModelPackage.Log;

import ModelPackage.Product.Product;
import ModelPackage.Users.User;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Setter @Getter
public class SellLog extends Log {
    //private Product product;
    private int moneyGotten;
    private ArrayList<Product> soldProducts;
    private int discount;
    private User buyer;

    public SellLog(ArrayList<Product> soldProducts, int moneyGotten, int discount, User buyer, Date date, DeliveryStatus deliveryStatus) {
        super(date, deliveryStatus);
        this.soldProducts = soldProducts;
        //this.product = product;
        this.moneyGotten = moneyGotten;
        this.discount = discount;
        this.buyer = buyer;
    }
}
