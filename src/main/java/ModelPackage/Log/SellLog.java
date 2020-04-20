package ModelPackage.Log;

import ModelPackage.Product.Product;
import ModelPackage.Users.User;
import lombok.*;

import java.util.Date;

@Setter @Getter
public class SellLog extends Log {
    private Product product;
    private int moneyGotten;
    private int discount;
    private User buyer;

    public SellLog(Product product, int moneyGotten, int discount, User buyer, String logId, Date date, DeliveryStatus deliveryStatus) {
        super(logId, date, deliveryStatus);
        this.product = product;
        this.moneyGotten = moneyGotten;
        this.discount = discount;
        this.buyer = buyer;
    }
}
