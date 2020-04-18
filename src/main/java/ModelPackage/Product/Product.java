package ModelPackage.Product;

import ModelPackage.Users.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private Company company;
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
}
