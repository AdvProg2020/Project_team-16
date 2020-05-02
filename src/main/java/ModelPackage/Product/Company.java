package ModelPackage.Product;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "t_company")
public class Company {
    @Id @GeneratedValue
    private int id;

    @Column(name = "NAME_OF_COMPANY")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "TypeOfProduct")
    private String group;

    @ElementCollection(targetClass = Product.class)
        @OneToMany
    private List<Product> productsIn;

    public Company(String name, String phone, String group) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.productsIn = new ArrayList<>();
    }

    public Company(String name, String phone, String group, ArrayList<Product> productsIn) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.productsIn = productsIn;
    }
}
