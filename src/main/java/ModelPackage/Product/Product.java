package ModelPackage.Product;

import ModelPackage.Users.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data @AllArgsConstructor
@Entity
@Table(name = "t_product")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID")
    private int id;

    @Column(name = "PRODUCT_String_ID")
    private String productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCT_STATUS")
    private ProductStatus productStatus;

    @Column(name = "NAME")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_ADDED")
    private Date dateAdded;

    @ManyToOne
    private Company companyClass;
    @Transient
    private String company;
    @Transient
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
        @OneToMany
    private List<Score> allScores;

    @Column(name = "TOTAL_SCORE")
    private double totalScore;

    @ElementCollection(targetClass = Comment.class)
        @OneToMany
    private List<Comment> allComments;

    @ElementCollection
        @JoinTable(name = "t_product_stock")
        @MapKeyColumn(name = "STOCK")
    private Map<String,Integer> stock;

    @ElementCollection
        @JoinTable(name = "t_product_prices")
        @MapKeyColumn(name = "PRICE")
    private Map<String,Integer> prices;

    @Column(name = "VIEW")
    private int view;

    @Column(name = "BOUGHT_AMOUNT")
    private int boughtAmount;

    @Column(name = "LEAST_PRICE")
    private int leastPrice;

    public Product(){
        this.productId = generateId();
    }

    public Product(String id){this.productId = id;}

    public Product(String name, String company, ArrayList<Seller> allSellers, String categoryId, HashMap<String, String> publicFeatures, HashMap<String, String> specialFeatures, String description, HashMap<String, Integer> stock, HashMap<String, Integer> prices) {
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
        this.productId = generateId();
    }

    public Product(Product product){
        this.productId = product.productId;
        this.productStatus = product.productStatus;
        this.name = product.name;
        this.dateAdded = product.dateAdded;
        this.company = product.company;
        this.allSellers = product.allSellers;
        this.category = product.category;
        this.categoryId = product.categoryId;
        this.publicFeatures = new HashMap<>(product.publicFeatures);
        this.specialFeatures = new HashMap<>(product.specialFeatures);
        this.description = product.description;
        this.allScores = new ArrayList<>(product.allScores);
        this.totalScore =product.totalScore;
        this.allComments = new ArrayList<>(product.allComments);
        this.stock = new HashMap<>(product.stock);
        this.prices = new HashMap<>(product.prices);
        this.view = product.view;
        this.boughtAmount = product.boughtAmount;
    }

    private String generateId(){
        Date date = new Date();
        return String.format("PR%s%04d",date.toString().replaceAll("\\s","").replaceAll(":",""),(int)(Math.random()*9999+1));
    }
}
