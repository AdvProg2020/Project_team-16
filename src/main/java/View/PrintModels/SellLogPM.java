package View.PrintModels;

import ModelPackage.Log.DeliveryStatus;
import lombok.Data;

@Data
public class SellLogPM {
    private int productId;
    private int moneyGotten;
    private int discount;
    private String buyer;
    private DeliveryStatus deliveryStatus;

    public SellLogPM(int productId, int moneyGotten, int discount, String buyer, DeliveryStatus deliveryStatus) {
        this.productId = productId;
        this.moneyGotten = moneyGotten;
        this.discount = discount;
        this.buyer = buyer;
        this.deliveryStatus = deliveryStatus;
    }
}
