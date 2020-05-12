package ModelPackage.Off;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_off_edit_attributes")
public class OffChangeAttributes {
    @Id @GeneratedValue
    private int id;

    private int target;

    @Temporal(TemporalType.DATE)
    Date start;

    @Temporal(TemporalType.DATE)
    Date end;

    int percentage;

    int productIdToRemove;

    int productIdToAdd;
}