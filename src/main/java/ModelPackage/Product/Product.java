package ModelPackage.Product;
import ModelPackage.Users.Seller;
import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.bridge.builtin.EnumBridge;

import javax.persistence.*;
import java.util.*;

@Data @AllArgsConstructor
@Entity
@Table(name = "t_product")
@Indexed
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCT_STATUS")
    @Field(bridge = @FieldBridge(impl = EnumBridge.class))
    private ProductStatus productStatus;

    @Column(name = "NAME")
    @Field
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_ADDED",updatable = false)
    private Date dateAdded;

    @ManyToOne
    private Company companyClass;

    @Column
    private String company;

    @ManyToOne
    private Category category;

    @ElementCollection
        @JoinTable(name = "t_product_public_feature")
        @MapKeyColumn(name = "feature")
    private Map<String,String> publicFeatures;

    @ElementCollection
        @JoinTable(name = "t_product_special_feature")
        @MapKeyColumn(name = "feature")
    private Map<String,String> specialFeatures;

    @Column(name = "DESCRIPTION")
    private String description;

    @ElementCollection(targetClass = Score.class)
        @OneToMany(cascade = CascadeType.ALL)
    private List<Score> allScores;

    @Column(name = "TOTAL_SCORE")
    private double totalScore;

    @ElementCollection(targetClass = Comment.class)
        @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> allComments;

    @Column(name = "VIEW")
    private int view;

    @Column(name = "BOUGHT_AMOUNT")
    private int boughtAmount;

    @Column(name = "LEAST_PRICE")
    private int leastPrice;

    @OneToMany
    private List<SellPackage> packages;

    public Product(int id){this.id = id;}

    public Product(String name, Company company, Category category, HashMap<String, String> publicFeatures, HashMap<String, String> specialFeatures, String description, SellPackage sellPackage) {
        this.name = name;
        this.companyClass = company;
        this.category = category;
        this.publicFeatures = publicFeatures;
        this.specialFeatures = specialFeatures;
        this.description = description;;
        this.allComments = new ArrayList<>();
        this.productStatus = ProductStatus.UNDER_CREATION;
        this.dateAdded = new Date();
        this.allScores = new ArrayList<>();
        this.view = 0;
        this.boughtAmount = 0;
        this.totalScore = 0;
        this.packages = new ArrayList<>();
        this.packages.add(sellPackage);
    }

    public Product() {
        this.allScores = new ArrayList<>();
        this.allComments = new ArrayList<>();
    }

    public SellPackage findPackageBySeller(String username) throws NoSuchSellerException{
        for (SellPackage sellPackage : packages) {
            if (sellPackage.getSeller().getUsername().equals(username))return sellPackage;
        }
        throw new NoSuchSellerException();
    }

    public SellPackage findPackageBySeller(Seller seller) throws NoSuchSellerException{
        for (SellPackage sellPackage : packages) {
            if (sellPackage.getSeller().equals(seller))return sellPackage;
        }
        throw new NoSuchSellerException();
    }

    public boolean hasSeller(Seller seller){
        for (SellPackage sellPackage : packages) {
            if (sellPackage.getSeller().equals(seller))return true;
        }
        return false;
    }

    public boolean hasSeller(String username){
        for (SellPackage sellPackage : packages) {
            if (sellPackage.getSeller().getUsername().equals(username))return true;
        }
        return false;
    }

    public boolean isOnOff() {
        for (SellPackage sellPackage : packages) {
            if (sellPackage.isOnOff()) return true;
        }
        return false;
    }
}
