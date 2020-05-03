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
        this.logId = generateId();
    }
    private String generateId(){
        Date date = new Date();
        return String.format("Log%s%04d",date.toString().replaceAll("\\s","").replaceAll(":",""),(int)(Math.random()*9999+1));
    }
}

