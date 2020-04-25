package ModelPackage.Log;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Log {
    private String logId;
    private Date date;
    private DeliveryStatus deliveryStatus;

    public Log(Date date, DeliveryStatus deliveryStatus) {
        this.logId = generateId();
        this.date = date;
        this.deliveryStatus = deliveryStatus;
    }

    private static String generateId(){
        Date date = new Date();
        return String.format("Log%s%04d",date.toString().replaceAll("\\s | ':'",""),(int)(Math.random()*9999+1));
    }
}
