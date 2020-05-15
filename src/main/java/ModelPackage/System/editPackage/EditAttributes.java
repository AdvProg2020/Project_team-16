package ModelPackage.System.editPackage;

import javax.persistence.*;

@Entity
@Table(name = "t_edit_attributes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EditAttributes {
    @Id @GeneratedValue @Column(name = "ID",updatable = false)
    protected int id;

    protected int sourceId;
}
