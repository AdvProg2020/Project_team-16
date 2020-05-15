package View.PrintModels;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class OrderLogPM {
    private Date date;
    private ArrayList<MiniProductPM> productPMs;
    private String deliveryStatus;
    private long price;
    private long paid;
    private int discount;

    public OrderLogPM(Date date, ArrayList<MiniProductPM> productPMs, String deliveryStatus, long price, long paid, int discount) {
        this.date = date;
        this.productPMs = productPMs;
        this.deliveryStatus = deliveryStatus;
        this.price = price;
        this.paid = paid;
        this.discount = discount;
    }
}
