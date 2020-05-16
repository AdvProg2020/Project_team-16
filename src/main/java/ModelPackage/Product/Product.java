package ModelPackage.Product;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Off.Off;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.Users.Seller;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
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
    @Id @GeneratedValue @Column(name = "ID")
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

    @Transient
    private String company;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL,targetEntity = Seller.class)
    private List<Seller> allSellers;

    @ManyToOne
    private Category category;

    @Column(name = "CAT_ID")
    private String categoryId;

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

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
        @OneToMany(targetEntity = SellerIntegerMap.class,cascade = CascadeType.ALL)
        @JoinTable(name = "t_product_stock")
    private List<SellerIntegerMap> stock;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
        @OneToMany(targetEntity = SellerIntegerMap.class,cascade = CascadeType.ALL)
        @JoinTable(name = "t_product_prices")
    private List<SellerIntegerMap> prices;

    @Column(name = "VIEW")
    private int view;

    @Column(name = "BOUGHT_AMOUNT")
    private int boughtAmount;

    @Column(name = "LEAST_PRICE")
    private int leastPrice;

    private boolean isOnOff = false;

    @OneToOne
    private Off off;

    public Product(int id){this.id = id;}

    public Product(String name, String company, ArrayList<Seller> allSellers, String categoryId, HashMap<String, String> publicFeatures, HashMap<String, String> specialFeatures, String description, List<SellerIntegerMap> stock, List<SellerIntegerMap> prices) {
        this.name = name;
        this.company = company;
        this.allSellers = allSellers;
        this.categoryId = categoryId;
        this.publicFeatures = publicFeatures;
        this.specialFeatures = specialFeatures;
        this.description = description;
        this.stock = stock;
        this.prices = prices;
        this.allComments = new ArrayList<>();
        this.productStatus = ProductStatus.UNDER_CREATION;
        this.dateAdded = new Date();
        this.allScores = new ArrayList<>();
        this.view = 0;
        this.boughtAmount = 0;
        this.totalScore = 0;
    }

    public Product() {

    }
}
