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

    public Company(String name, String phone, String group) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.productsIn = new ArrayList<Product>();
    }

    public Company(String name, String phone, String group, ArrayList<Product> productsIn) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.productsIn = productsIn;
    }
}
