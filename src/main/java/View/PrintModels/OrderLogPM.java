package View.PrintModels;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class OrderLogPM {
    private int orderId;
    private String date;
    private ArrayList<OrderProductPM> productPMs;
    private String deliveryStatus;
    private long price;
    private long paid;
    private int discount;

    public OrderLogPM(int orderId, String date, ArrayList<OrderProductPM> productPMs, String deliveryStatus, long price, long paid, int discount) {
        this.orderId = orderId;
        this.date = date;
        this.productPMs = productPMs;
        this.deliveryStatus = deliveryStatus;
        this.price = price;
        this.paid = paid;
        this.discount = discount;
    }
}
