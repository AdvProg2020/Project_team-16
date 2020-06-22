package ModelPackage.System.editPackage;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_product_edit_attributes")
public class ProductEditAttribute extends EditAttributes{
    private String name;
    @ElementCollection
    private Map<String, String> publicFeatures;
    @ElementCollection
    private Map<String, String> specialFeatures;
    private int newStock;
    private int newPrice;
    private int newCategoryId;
}
