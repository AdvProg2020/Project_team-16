package ModelPackage.Log;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Log {
    private String logId;
    private Date date;
    private DeliveryStatus deliveryStatus;

    public Log(Date date, DeliveryStatus deliveryStatus) {
        this.date = date;
        this.deliveryStatus = deliveryStatus;
    }
}

