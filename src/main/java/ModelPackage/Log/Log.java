package ModelPackage.Log;

import lombok.Data;

import java.util.Date;

@Data
abstract class Log {
    private String logId;
    private Date date;
    private DeliveryStatus deliveryStatus;
}

enum DeliveryStatus{
    DELIVERED,
    DEOENDING;
}
