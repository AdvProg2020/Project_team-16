package ModelPackage.Product;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "t_category")
public class Category {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CAT_ID")
    private String categoryId;

    @ElementCollection
    @Column(name = "SPECIAL_FEATURES")
    private List<String> specialFeatures;

    @ElementCollection(targetClass = Category.class)
        @OneToMany
    private List<Category> subCategories;

    @ManyToOne
    private Category parent;

    @Column(name = "PARENT__ID")
    private String parentId;

    @ElementCollection(targetClass = Product.class)
        @OneToMany
    private List<Product> allProducts;

    public Category(String name, String parentId) {
        this.name = name;
        this.categoryId = idGenerator();
        this.parentId = parentId;
        this.specialFeatures = new ArrayList<String>();
        this.subCategories  = new ArrayList<Category>();
        this.allProducts = new ArrayList<>();
    }

    private String idGenerator(){
        Date date = new Date();
        return String.format("CT%s%04d",date.toString().replaceAll("\\s","").replaceAll(":",""),(int)(Math.random()*9999+1));
    }
}
