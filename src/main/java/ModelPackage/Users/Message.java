package ModelPackage.Users;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@Table(name = "t_massage")
public class Message {
    @Id @GeneratedValue
    private int id;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "MESSAGE")
    private String message;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE")
    private Date date;

    @Column(name = "IS_READ")
    boolean isRead;
}
