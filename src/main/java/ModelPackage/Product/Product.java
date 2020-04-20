package ModelPackage.Product;

import ModelPackage.Users.Seller;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Data @Builder
public class Product {
    private String productId;
    private ProductStatus productStatus;
    private String name;
    private Date dateAdded;
    private Company company;
    private ArrayList<Seller> allSellers;
    private Category category;
    private String categoryId;
    private HashMap<String,String> publicFeatures;
    private HashMap<String,String> specialFeatures;
    private String description;
    private ArrayList<Score> allScores;
    private float totalScore;
    private ArrayList<Comment> allComments;
    private HashMap<String,Integer> stock;
    private HashMap<String,Integer> prices;
    private int view;
    private int boughtAmount;
                                    /*should be deleted before merge*/
    public Product(String productId, ProductStatus productStatus, String name, Date dateAdded, Company company, ArrayList<Seller> allSellers, Category category, String categoryId, HashMap<String, String> publicFeatures, HashMap<String, String> specialFeatures, String description, ArrayList<Score> allScores, float totalScore, ArrayList<Comment> allComments, HashMap<String, Integer> stock, HashMap<String, Integer> prices, int view, int boughtAmount) {
        this.productId = productId;
        this.productStatus = productStatus;
        this.name = name;
        this.dateAdded = dateAdded;
        this.company = company;
        this.allSellers = allSellers;
        this.category = category;
        this.categoryId = categoryId;
        this.publicFeatures = publicFeatures;
        this.specialFeatures = specialFeatures;
        this.description = description;
        this.allScores = allScores;
        this.totalScore = totalScore;
        this.allComments = allComments;
        this.stock = stock;
        this.prices = prices;
        this.view = view;
        this.boughtAmount = boughtAmount;
    }
}
