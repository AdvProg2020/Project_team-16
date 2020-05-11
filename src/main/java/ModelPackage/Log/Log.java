package ModelPackage.Log;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "t_log")
public abstract class Log {
    @Id @GeneratedValue
    private int logId;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "DELIVERY_STATUS")
    private DeliveryStatus deliveryStatus;

    public Log(Date date, DeliveryStatus deliveryStatus) {
        this.date = date;
        this.deliveryStatus = deliveryStatus;
    }
    private String generateId(){
        Date date = new Date();
        return String.format("Log%s%04d",date.toString().replaceAll("\\s","").replaceAll(":",""),(int)(Math.random()*9999+1));
    }
}