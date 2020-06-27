package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.System.editPackage.ProductEditAttribute;
import ModelPackage.System.exeption.product.*;
import ModelPackage.Users.*;
import View.PrintModels.MicroProduct;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ProductManager {
    private static ProductManager productManager = null;

    List<Product> getAllProductsActive() {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.equal(root.get("productStatus").as(ProductStatus.class), ProductStatus.VERIFIED)
        );
        Query<Product> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private ProductManager() {
    }

    public static ProductManager getInstance(){
        if(productManager == null){
            return productManager = new ProductManager();
        }
        else{
            return productManager;
        }
    }

    public int createProduct(Product product, String sellerId) {
        product.setView(0);
        product.setBoughtAmount(0);
        product.setTotalScore(0);
        DBManager.save(product);
        String requestStr = String.format("%s has requested to create Product \"%s\" with id %s",sellerId,product.getName(),product.getId());
        Seller seller = DBManager.load(Seller.class,sellerId);
        Request request = new Request(seller.getUsername(), RequestType.CREATE_PRODUCT,requestStr,product);
        RequestManager.getInstance().addRequest(request);
        seller.addRequest(request);
        DBManager.save(request);
        return product.getId();
    }

    public void editProduct(ProductEditAttribute edited, String editor) throws NoSuchAProductException, EditorIsNotSellerException {
        String requestStr = String.format("%s has requested to edit Product \"%s\" with id %s",edited,edited.getName(),edited.getId());
        Product product = findProductById(edited.getSourceId());
        System.out.println(edited.getName());
        DBManager.save(edited);
        checkIfEditorIsASeller(editor,product);
        product.setProductStatus(ProductStatus.UNDER_EDIT);
        Request request = new Request(editor,RequestType.CHANGE_PRODUCT,requestStr,edited);
        RequestManager.getInstance().addRequest(request);
        Seller seller = DBManager.load(Seller.class, editor);
        if (seller != null) {
            seller.addRequest(request);
            DBManager.save(seller);
        }
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
        for (Product product : getAllProductsActive()) {
            if(product.getName().toLowerCase().contains(name.toLowerCase()))
                list.add(new MicroProduct(product.getName(),product.getId()));
        }
        return list;
    }

    public void addView(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        product.setView(product.getView()+1);
        DBManager.save(product);
    }

    public void addBought(int productId, int amount) throws NoSuchAProductException {
        Product product = findProductById(productId);
        product.setBoughtAmount(product.getBoughtAmount() + amount);
        DBManager.save(product);
    }

    public void assignAComment(int productId, Comment comment) throws NoSuchAProductException {
        Product product = findProductById(productId);
        List<Comment> comments = product.getAllComments();
        comments.add(comment);
        product.setAllComments(comments);
        DBManager.save(product);
        DBManager.save(comment);
    }

    public void assignAScore(int productId, Score score) throws NoSuchAProductException {
        Product product = findProductById(productId);
        List<Score> scores = product.getAllScores();
        int amount = scores.size();
        scores.add(score);
        product.setAllScores(scores);
        product.setTotalScore((product.getTotalScore()*amount + score.getScore())/(amount+1));
        DBManager.save(product);
    }

    public Comment[] showComments(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        List<Comment> comments = new CopyOnWriteArrayList<>(product.getAllComments());
        Iterator<Comment> iterator = comments.iterator();
        while (iterator.hasNext()) {
            Comment comment = iterator.next();
            if (!comment.getStatus().equals(CommentStatus.VERIFIED)) comments.remove(comment);
        }
        Comment[] toReturn = new Comment[comments.size()];
        comments.toArray(toReturn);
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

    public void deleteProduct(int productId) throws NoSuchAProductException {
        Product product = findProductById(productId);
        deleteProduct(product);
    }

    public void deleteProduct(Product product) {
        List<SellPackage> packages = product.getPackages();
        product.setPackages(new ArrayList<>());
        DBManager.save(product);
        packages.forEach(this::deleteSellPackage);
        deleteAllRequestRelatedToProduct(product);
        Category category = product.getCategory();
        category.getAllProducts().remove(product);
        DBManager.save(category);
        product.setCategory(null);
        product.setCompanyClass(null);
        deletePictures(product.getId());
    }

    private void deletePictures(int id) {
        File directory = new File("src/main/resources/db/images/products/" + id);
        try {
            FileUtils.deleteDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllRequestRelatedToProduct(Product product) {
        List<Request> requests = getAllRequests(product);
        requests.forEach(request -> {
            request.setProduct(null);
            DBManager.save(request);
        });
    }

    private List<Request> getAllRequests(Product product) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.equal(root.get("product").as(Product.class), product)
        );
        Query<Request> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private void deleteSellPackage(SellPackage sellPackage) {
        Seller seller = sellPackage.getSeller();
        if (sellPackage.isOnOff()) {
            Off off = sellPackage.getOff();
            off.getProducts().remove(sellPackage.getProduct());
            DBManager.save(off);
            sellPackage.setOff(null);
        }
        List<SellPackage> packages = seller.getPackages();
        packages.remove(sellPackage);
        seller.setPackages(new ArrayList<>(packages));
        Product product = sellPackage.getProduct();
        deleteAllSubCarts(seller, product);
        sellPackage.setProduct(null);
        sellPackage.setSeller(null);
        DBManager.save(product);
        DBManager.save(seller);
        DBManager.delete(sellPackage);
    }

    private void deleteAllSubCarts(Seller seller, Product product) {
        List<SubCart> subCarts = getAllSubCarts(seller, product);
        subCarts.forEach(subCart -> {
            Cart cart = subCart.getCart();
            cart.getSubCarts().remove(subCart);
            DBManager.save(cart);
            DBManager.delete(subCart);
        });
    }

    private List<SubCart> getAllSubCarts(Seller seller, Product product) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<SubCart> criteriaQuery = criteriaBuilder.createQuery(SubCart.class);
        Root<SubCart> root = criteriaQuery.from(SubCart.class);
        criteriaQuery.select(root);
        Predicate[] predicates = {
                criteriaBuilder.equal(root.get("seller").as(Seller.class), seller),
                criteriaBuilder.equal(root.get("product").as(Product.class), product)
        };
        criteriaQuery.where(
                predicates
        );
        Query<SubCart> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public void changePrice(Product product, int newPrice, String username) throws NoSuchSellerException {
        SellPackage sellPackage = product.findPackageBySeller(username);
        if (newPrice > 0) {
            sellPackage.setPrice(newPrice);
            DBManager.save(sellPackage);
        }
        if (newPrice < product.getLeastPrice()) {
            product.setLeastPrice(newPrice);
        }
        DBManager.save(product);
    }

    public void changeStock(Product product, int newStock, String username) throws NoSuchSellerException {
        SellPackage sellPackage = product.findPackageBySeller(username);
        if (newStock > 0) {
            sellPackage.setStock(newStock);
            DBManager.save(sellPackage);
        }
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
        SellPackage sellPackage = new SellPackage(product, seller, price, amount, null, false, price != 0);
        DBManager.save(sellPackage);
        int currentLeast = product.getLeastPrice();
        if (currentLeast > price) {
            product.setLeastPrice(price);
        }
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

    public List<Product> getAllOffFromActiveProducts(){
        List<Product> toReturn = new ArrayList<>();
        for (Product product : getAllProductsActive()) {
            if (product.isOnOff())toReturn.add(product);
        }
        return toReturn;
    }
}
