package ModelPackage.System;

import ModelPackage.Product.Comment;
import ModelPackage.Product.Product;
import ModelPackage.Product.Score;
import ModelPackage.Users.Seller;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ProductManager {
    private ArrayList<Product> allProducts;
    private static ProductManager productManager = null;

    private ProductManager(){
        allProducts = new ArrayList<>();
    }

    public static ProductManager getInstance(){
        if(productManager == null){
            return productManager = new ProductManager();
        }
        else{
            return productManager;
        }
    }

    public void addAmountOfStock(String productId, String sellerId,int amount){
        /* TODO : Add Exception of Negative Stock */
        Product product = findProductById(productId);
        HashMap<String, Integer> stock = product.getStock();
        stock.replace(sellerId, stock.get(sellerId) + amount);
        product.setStock(stock);
    }

    public Product findProductById(String id){
        /* TODO : NULL POINTER EXCEPTION */
        for (Product product : allProducts) {
            if(product.getProductId().equals(id)) return product;
        }
        return null;
    }

    public Product[] findProductByName(String name){
        List<Product> list = new ArrayList<>();
        for (Product product : allProducts) {
            if(product.getName().toLowerCase().contains(name.toLowerCase()))list.add(product);
        }
        Product[] toReturn = new Product[list.size()];
        list.toArray(toReturn);
        return toReturn;
    }

    public void addProductToList(Product product){
        allProducts.add(product);
    }

    public void addView(String productId){
        Product product = findProductById(productId);
        product.setView(product.getView()+1);
    }

    public void addBought(String productId){
        Product product = findProductById(productId);
        product.setBoughtAmount(product.getBoughtAmount()+1);
    }

    public void assignAComment(String productId, Comment comment){
        Product product = findProductById(productId);
        ArrayList<Comment> comments = product.getAllComments();
        comments.add(comment);
        product.setAllComments(comments);
    }

    public void assignAScore(String productId, Score score){
        Product product = findProductById(productId);
        ArrayList<Score> scores = product.getAllScores();
        int amount = scores.size();
        scores.add(score);
        product.setAllScores(scores);
        product.setTotalScore((product.getTotalScore()*amount + score.getScore())/(amount+1));
    }

    public Comment[] showComments(String productId){
        Product product = findProductById(productId);
        ArrayList<Comment> comments = product.getAllComments();
        Comment[] toReturn = new Comment[comments.size()];
        comments.toArray(toReturn);
        return toReturn;
    }

    public void clear(){
        allProducts.clear();
    }
}
