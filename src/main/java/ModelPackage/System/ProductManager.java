package ModelPackage.System;

import ModelPackage.Product.Comment;
import ModelPackage.Product.Product;
import ModelPackage.Product.ProductStatus;
import ModelPackage.Product.Score;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.product.*;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;
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

    public void createProduct(Product product,String sellerId){
        String requestStr = String.format("%s has requested to create Product \"%s\" with id %s",sellerId,product.getName(),product.getProductId());
        Request request = new Request(sellerId, RequestType.CREATE_PRODUCT,requestStr,product);
        RequestManager.getInstance().addRequest(request);
    }

    public void editProduct(Product edited,String editor){
        String requestStr = String.format("%s has requested to edit Product \"%s\" with id %s",edited,edited.getName(),edited.getProductId());
        Product product = findProductById(edited.getProductId());
        product.setProductStatus(ProductStatus.UNDER_EDIT);
        Request request = new Request(editor,RequestType.CHANGE_PRODUCT,requestStr,edited);
        RequestManager.getInstance().addRequest(request);
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

    public Score[] showScores(String productId){
        Product product = findProductById(productId);
        ArrayList<Score> scores = product.getAllScores();
        Score[] toReturn = new Score[scores.size()];
        scores.toArray(toReturn);
        return toReturn;
    }

    public boolean doesThisProductExist(String productId){
        Product product = findProductById(productId);
        return (product != null);
    }

    public void checkIfThisProductExists(String productId) throws NoSuchAProductException{
        Product product = findProductById(productId);
        if (product == null) throw new NoSuchAProductException(productId);
    }

    public boolean isThisProductAvailable(String id){
        Product product = findProductById(id);
        ProductStatus productStatus = product.getProductStatus();
        return productStatus == ProductStatus.VERIFIED;
    }

    public int leastPriceOf(String productId){
        Product product = findProductById(productId);
        HashMap<String,Integer> prices = product.getPrices();
        int leastPrice = 2147483647;
        for (Integer value : prices.values()) {
            if(leastPrice > value) leastPrice = value;
        }
        return leastPrice;
    }

    public void deleteProduct(String productId)
            throws NoSuchACategoryException, NoSuchAProductInCategoryException {
        Product product = findProductById(productId);
        allProducts.remove(product);
        CategoryManager.getInstance().removeProductFromCategory(productId,product.getCategoryId());

        /* TODO : delete from database */
    }

    public void deleteProductCategoryOrder(String productId){
        Product product = findProductById(productId);
        allProducts.remove(product);
        /* TODO : delete from database */
    }

    public HashMap<String,String> allFeaturesOf(Product product){
        HashMap<String,String> allFeatures = new HashMap<>(product.getPublicFeatures());
        allFeatures.putAll(product.getSpecialFeatures());
        return allFeatures;
    }

    public void clear(){
        allProducts.clear();
    }
}
