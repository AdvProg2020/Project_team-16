package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data public class OrderMiniLog {
    private Date dates;
    private int orderId;
    private long paid;

    public OrderMiniLog(Date dates, int orderId, long paid) {
        this.dates = dates;
        this.orderId = orderId;
        this.paid = paid;
    }
}
