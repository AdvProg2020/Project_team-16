package ModelPackage.Product;

import ModelPackage.Users.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Data @AllArgsConstructor
public class Product {
    private String productId;
    private ProductStatus productStatus;
    private String name;
    private Date dateAdded;
    private String company;
    private ArrayList<Seller> allSellers;
    private Category category;
    private String categoryId;
    private HashMap<String,String> publicFeatures;
    private HashMap<String,String> specialFeatures;
    private String description;
    private ArrayList<Score> allScores;
    private double totalScore;
    private ArrayList<Comment> allComments;
    private HashMap<String,Integer> stock;
    private HashMap<String,Integer> prices;
    private int view;
    private int boughtAmount;
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
        /* TODO : find category and set */
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
