package ModelPackage.System;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.ProductEditAttribute;
import ModelPackage.System.exeption.account.ProductNotHaveSellerException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.product.*;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;
import ModelPackage.Users.Seller;
import View.PrintModels.MicroProduct;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Data
public class ProductManager {
    private List<Product> allProductsActive;
    private static ProductManager productManager = null;

    private ProductManager(){
        if (allProductsActive == null)
            allProductsActive = new ArrayList<>();
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
        Request request = new Request(seller.getUsername(), RequestType.CREATE_PRODUCT,requestStr,product);
        RequestManager.getInstance().addRequest(request);
    }

    public void editProduct(ProductEditAttribute edited, String editor) throws NoSuchAProductException, EditorIsNotSellerException {
        String requestStr = String.format("%s has requested to edit Product \"%s\" with id %s",edited,edited.getName(),edited.getId());
        Product product = findProductById(edited.getSourceId());
        checkIfEditorIsASeller(editor,product);
        allProductsActive.remove(product);
        product.setProductStatus(ProductStatus.UNDER_EDIT);
        Request request = new Request(editor,RequestType.CHANGE_PRODUCT,requestStr,edited);
        RequestManager.getInstance().addRequest(request);
    }

    private void checkIfEditorIsASeller(String username,Product product) throws EditorIsNotSellerException {
        if (!product.hasSeller(username))throw new EditorIsNotSellerException();
    }

    public void changeAmountOfStock(int productId, String sellerId, int amount) throws NoSuchSellerException {
        Product product = DBManager.load(Product.class,productId);
        SellPackage sellPackage = product.findPackageBySeller(sellerId);
        int stock = sellPackage.getStock();
        stock += amount;
        if (stock < 0) stock = 0;
        sellPackage.setStock(stock);
        DBManager.save(sellPackage);
    }

    public Product findProductById(int id) throws NoSuchAProductException {
        Product product = DBManager.load(Product.class,id);
        if (product == null) {
            throw new NoSuchAProductException(Integer.toString(id));
        }
        return product;
    }

    public ArrayList<MicroProduct> findProductByName(String name){
        ArrayList<MicroProduct> list = new ArrayList<>();
        for (Product product : allProductsActive) {
            if(product.getName().toLowerCase().contains(name.toLowerCase()))
                list.add(new MicroProduct(product.getName(),product.getId()));
        }
        return list;
    }

    public int getProductIdByName(String name){
        for (Product product : allProductsActive) {
            if (product.getName().equals(name)){
                return product.getId();
            }
        }
        return -1;
    }

    public void addView(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        product.setView(product.getView()+1);
        DBManager.save(product);
    }

    public void addBought(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        product.setBoughtAmount(product.getBoughtAmount()+1);
        DBManager.save(product);
    }

    public void assignAComment(int productId, Comment comment) throws NoSuchAProductException {
        Product product = findProductById(productId);
        ArrayList<Comment> comments = (ArrayList<Comment>) product.getAllComments();
        comments.add(comment);
        product.setAllComments(comments);
        DBManager.save(product);
    }

    public void assignAScore(int productId, Score score) throws NoSuchAProductException {
        Product product = findProductById(productId);
        ArrayList<Score> scores = (ArrayList<Score>) product.getAllScores();
        int amount = scores.size();
        scores.add(score);
        product.setAllScores(scores);
        product.setTotalScore((product.getTotalScore()*amount + score.getScore())/(amount+1));
        DBManager.save(product);
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
        return product.getLeastPrice();
    }

    public Seller getSellerOfProduct(int productId, String sellerUserName)
            throws NoSuchAProductException, NoSuchSellerException {
        Product product = findProductById(productId);
        return product.findPackageBySeller(sellerUserName).getSeller();
    }

    public void deleteProduct(int productId,String remover)
            throws NoSuchACategoryException, NoSuchAProductInCategoryException, NoSuchAProductException, EditorIsNotSellerException {
        Product product = findProductById(productId);
        if (!remover.equals("MAN@GER"))checkIfEditorIsASeller(remover,product);
        CategoryManager.getInstance().removeProductFromCategory(productId,product.getId());
        CSCLManager.getInstance().removeProductFromCompany(product);
        DBManager.delete(product);
    }

    public void deleteProductCategoryOrder(Product product){
        DBManager.delete(product);
    }

    public HashMap<String,String> allFeaturesOf(Product product){
        HashMap<String,String> allFeatures = new HashMap<>(product.getPublicFeatures());
        allFeatures.putAll(product.getSpecialFeatures());
        return allFeatures;
    }

    public void addASellerToProduct(Product product,Seller seller,int amount,int price) {
        SellPackage sellPackage = new SellPackage(product,seller,price,amount,null, false,true);
        DBManager.save(sellPackage);
        product.getPackages().add(sellPackage);
        seller.getPackages().add(sellPackage);
        DBManager.save(seller);
        DBManager.save(product);
    }

    public Seller bestSellerOf(Product product){
        Seller seller = new Seller();
        int pricy = 2000000000;
        for (SellPackage aPackage : product.getPackages()) {
            int price = aPackage.getPrice();
            if (price < pricy){
                seller = aPackage.getSeller();
                pricy = price;
            }
        }
        return seller;
    }

    public void clear(){
        allProductsActive.clear();
    }

    public void addToActive(Product product){
        allProductsActive.add(product);
    }

    public List<Product> getAllOffFromActiveProducts(){
        List<Product> toReturn = new ArrayList<>();
        for (Product product : allProductsActive) {
            if (product.isOnOff())toReturn.add(product);
        }
        return toReturn;
    }
}
