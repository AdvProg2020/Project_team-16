package ModelPackage.System.editPackage;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_product_edit_attributes")
public class ProductEditAttribute extends EditAttributes{
    private String name;
    private String publicFeatureTitle;
    private String publicFeature;
    private String onePublicFeatureRemove;
    private String specialFeatureTitle;
    private String specialFeature;
    private String oneSpecialFeatureRemove;
}
