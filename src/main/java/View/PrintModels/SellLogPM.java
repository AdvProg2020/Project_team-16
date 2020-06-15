package View.PrintModels;

import ModelPackage.Log.DeliveryStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SellLogPM {
    private int id;
    private int productId;
    private int moneyGotten;
    private int discount;
    private Date date;
    private String buyer;
    private DeliveryStatus deliveryStatus;

    public SellLogPM(int id, int productId, int moneyGotten, int discount, Date date, String buyer, DeliveryStatus deliveryStatus) {
        this.id = id;
        this.productId = productId;
        this.moneyGotten = moneyGotten;
        this.discount = discount;
        this.date = date;
        this.buyer = buyer;
        this.deliveryStatus = deliveryStatus;
    }
}
