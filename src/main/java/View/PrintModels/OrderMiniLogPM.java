package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data public class OrderMiniLogPM {
    private Date dates;
    private int orderId;
    private long paid;

    public OrderMiniLogPM(Date dates, int orderId, long paid) {
        this.dates = dates;
        this.orderId = orderId;
        this.paid = paid;
    }
}
