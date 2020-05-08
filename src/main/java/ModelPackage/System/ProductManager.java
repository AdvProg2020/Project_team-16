package ModelPackage.System;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.Comment;
import ModelPackage.Product.Product;
import ModelPackage.Product.ProductStatus;
import ModelPackage.Product.Score;
import ModelPackage.System.database.DBManager;
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
        DBManager.save(product);
        String requestStr = String.format("%s has requested to create Product \"%s\" with id %s",sellerId,product.getName(),product.getId());
        Seller seller = DBManager.load(Seller.class,sellerId);
        Request request = new Request(seller, RequestType.CREATE_PRODUCT,requestStr,product);
        //RequestManager.getInstance().addRequest(request);
    }

    public void editProduct(Product edited,String editor) throws NoSuchAProductException {
        String requestStr = String.format("%s has requested to edit Product \"%s\" with id %s",edited,edited.getName(),edited.getId());
        Product product = findProductById(edited.getId());
        product.setProductStatus(ProductStatus.UNDER_EDIT);
        Seller seller = DBManager.load(Seller.class,editor);
        Request request = new Request(seller,RequestType.CHANGE_PRODUCT,requestStr,edited);
        //RequestManager.getInstance().addRequest(request);
    }

    public void addAmountOfStock(int productId, String sellerId,int amount){
        Product product = DBManager.load(Product.class,productId);
        List<SellerIntegerMap> list = product.getStock();
        for (SellerIntegerMap map : list) {
            if(map.thisIsTheMapKey(sellerId)){
                map.setInteger(map.getInteger()+amount);
                break;
            }
        }
        DBManager.save(product);
    }

    public Product findProductById(int id) throws NoSuchAProductException {
        Product product = DBManager.load(Product.class,id);
        if (product == null) {
            throw new NoSuchAProductException(Integer.toString(id));
        }
        return product;
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

    public void addView(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        product.setView(product.getView()+1);
    }

    public void addBought(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        product.setBoughtAmount(product.getBoughtAmount()+1);
    }

    public void assignAComment(int productId, Comment comment) throws NoSuchAProductException {
        Product product = findProductById(productId);
        ArrayList<Comment> comments = (ArrayList<Comment>) product.getAllComments();
        comments.add(comment);
        product.setAllComments(comments);
    }

    public void assignAScore(int productId, Score score) throws NoSuchAProductException {
        Product product = findProductById(productId);
        ArrayList<Score> scores = (ArrayList<Score>) product.getAllScores();
        int amount = scores.size();
        scores.add(score);
        product.setAllScores(scores);
        product.setTotalScore((product.getTotalScore()*amount + score.getScore())/(amount+1));
    }

    public Comment[] showComments(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        ArrayList<Comment> comments = (ArrayList<Comment>) product.getAllComments();
        Comment[] toReturn = new Comment[comments.size()];
        comments.toArray(toReturn);
        return toReturn;
    }

    public Score[] showScores(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        ArrayList<Score> scores = (ArrayList<Score>)product.getAllScores();
        Score[] toReturn = new Score[scores.size()];
        scores.toArray(toReturn);
        return toReturn;
    }

    public boolean doesThisProductExist(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        return (product != null);
    }

    public void checkIfThisProductExists(int productId) throws NoSuchAProductException{
        Product product = findProductById(productId);
        if (product == null) throw new NoSuchAProductException(Integer.toString(productId));
    }

    public boolean isThisProductAvailable(int id) throws NoSuchAProductException {
        Product product = findProductById(id);
        ProductStatus productStatus = product.getProductStatus();
        return productStatus == ProductStatus.VERIFIED;
    }

    public int leastPriceOf(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        HashMap<String,Integer> prices = (HashMap<String, Integer>) product.getPrices();
        int leastPrice = 2147483647;
        for (Integer value : prices.values()) {
            if(leastPrice > value) leastPrice = value;
        }
        return leastPrice;
    }

    public void deleteProduct(int productId)
            throws NoSuchACategoryException, NoSuchAProductInCategoryException, NoSuchAProductException {
        Product product = findProductById(productId);
        allProducts.remove(product);
        //CategoryManager.getInstance().removeProductFromCategory(productId,product.getCategoryId());

        DBManager.delete(product);
    }

    public void deleteProductCategoryOrder(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        allProducts.remove(product);
        DBManager.delete(product);
    }

    public HashMap<String,String> allFeaturesOf(Product product){
        HashMap<String,String> allFeatures = new HashMap<>(product.getPublicFeatures());
        allFeatures.putAll(product.getSpecialFeatures());
        return allFeatures;
    }

    public void addASellerToProduct(int productId,Seller seller,int amount,int price)
            throws NoSuchAProductException, AlreadyASeller {
        Product product = findProductById(productId);
        List<Seller> sellers = product.getAllSellers();
        if (!sellers.contains(seller)){
            sellers.add(seller);
            addAmountToProductForNewSeller(seller,product,amount);
            addPriceToProductForNewSeller(seller,product,price);
        }
        else{
            throw new AlreadyASeller(seller.getUsername());
        }
    }

    private void addAmountToProductForNewSeller(Seller seller,Product product,int amount){
        SellerIntegerMap newStock = new SellerIntegerMap(seller,amount);
        DBManager.save(newStock);
        List<SellerIntegerMap> stocks = product.getStock();
        stocks.add(newStock);
        product.setStock(stocks);
    }

    private void addPriceToProductForNewSeller(Seller seller,Product product,int price){
        SellerIntegerMap newPrice = new SellerIntegerMap(seller,price);
        DBManager.save(newPrice);
        List<SellerIntegerMap> prices = product.getPrices();
        prices.add(newPrice);
        product.setStock(prices);
    }

    public void clear(){
        allProducts.clear();
    }
}
