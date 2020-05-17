package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data public class OrderMiniLogPM {
    private Date date;
    private int orderId;
    private long paid;

    public OrderMiniLogPM(Date date, int orderId, long paid) {
        this.date = date;
        this.orderId = orderId;
        this.paid = paid;
    }
}
