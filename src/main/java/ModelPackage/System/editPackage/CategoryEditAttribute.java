package ModelPackage.System.editPackage;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_category_edit")
public class CategoryEditAttribute extends EditAttributes {
    String name;
    String addFeature;
    String removeFeature;
    int newParentId;
}
