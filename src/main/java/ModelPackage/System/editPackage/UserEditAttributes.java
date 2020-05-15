package ModelPackage.System.editPackage;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_edit")
public class UserEditAttributes extends EditAttributes {
    String newPassword;
    String newFirstName;
    String newLastName;
    String newEmail;
    String newPhone;
}