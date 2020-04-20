package ModelPackage.Log;

import ModelPackage.Product.Product;
import ModelPackage.Users.User;
import lombok.*;

import java.util.Date;

@Setter @Getter
public class SellLOG extends LOG {
    private Product product;
    private int moneyGotten;
    private int discount;
    private User buyer;

    public SellLOG(Product product, int moneyGotten, int discount, User buyer, Date date, DeliveryStatus deliveryStatus) {
        //super(date, deliveryStatus);
        this.product = product;
        this.moneyGotten = moneyGotten;
        this.discount = discount;
        this.buyer = buyer;
    }
}
