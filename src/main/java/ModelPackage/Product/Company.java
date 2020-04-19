package ModelPackage.Product;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
