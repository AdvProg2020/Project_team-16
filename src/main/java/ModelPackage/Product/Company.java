package ModelPackage.Product;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data @Builder
public class Company {
    private String name;
    private String phone;
    private String group;
    private ArrayList<Product> productsIn;
}
