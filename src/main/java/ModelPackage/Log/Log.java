package ModelPackage.Log;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @Builder @NoArgsConstructor
abstract class Log {
    private String logId;
    private Date date;
    private DeliveryStatus deliveryStatus;
}

enum DeliveryStatus{
    DELIVERED,
    DEOENDING;
}
