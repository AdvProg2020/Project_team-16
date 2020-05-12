package ModelPackage.Product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "t_company")
public class Company {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @Column(name = "NAME_OF_COMPANY")
    private String name;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "TypeOfProduct")
    private String group;

    @ElementCollection(targetClass = Product.class)
    @LazyCollection(LazyCollectionOption.FALSE)
        @OneToMany(cascade = CascadeType.ALL)
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

    public Company(){

    }
}
