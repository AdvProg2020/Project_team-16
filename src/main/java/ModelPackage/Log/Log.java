package ModelPackage.Log;

import lombok.Data;

import java.util.Date;

@Data
abstract class Log {
    private String logId;
    private Date date;
    private DeliveryStatus deliveryStatus;

    public Log(String logId, Date date, DeliveryStatus deliveryStatus) {
        this.logId = logId;
        this.date = date;
        this.deliveryStatus = deliveryStatus;
    }
}
