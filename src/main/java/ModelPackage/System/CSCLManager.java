package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.Log;
import ModelPackage.Log.PurchaseLog;
import ModelPackage.Log.SellLog;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.clcsmanager.NoSuchACompanyException;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
import ModelPackage.System.exeption.clcsmanager.YouAreNotASellerException;
import ModelPackage.System.exeption.product.EditorIsNotSellerException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CSCLManager {
    private static CSCLManager csclManager = new CSCLManager();

    private CSCLManager() {}

    public static CSCLManager getInstance() {
        return csclManager;
    }

    public int createCompany(Company newCompany) {
        DBManager.save(newCompany);
        return newCompany.getId();
    }

    public Company getCompanyById(int id)
            throws NoSuchACompanyException {
        Company company = DBManager.load(Company.class,id);
        if (company == null) throw new NoSuchACompanyException(id);
        else return company;
    }

    public void editCompanyName(int id, String newName)
            throws NoSuchACompanyException {
        Company company = getCompanyById(id);
        company.setName(newName);
        DBManager.save(company);
    }

    public void editCompanyGroup(int id, String newGroup)
            throws NoSuchACompanyException {
        Company company = getCompanyById(id);
        company.setGroup(newGroup);
        DBManager.save(company);
    }

    public void addProductToCompany(int productId,int companyId)
            throws NoSuchACompanyException, NoSuchAProductException {
        Company company = getCompanyById(companyId);
        Product product = ProductManager.getInstance().findProductById(productId);
        company.getProductsIn().add(product);
        product.setCompanyClass(company);
        DBManager.save(product);
        DBManager.save(company);
    }

    public void removeProductFromCompany(int productId,int companyId)
            throws NoSuchACompanyException, NoSuchAProductException {
        Company company = getCompanyById(companyId);
        company.getProductsIn().remove(ProductManager.getInstance().findProductById(productId));
        DBManager.save(company);
    }

    public void removeProductFromCompany(Product product){
        Company company = product.getCompanyClass();
        company.getProductsIn().remove(product);
        DBManager.save(company);
    }

    public void createComment(Comment comment) {
        DBManager.save(comment);
        String requestStr = String.format("User (%s) has requested to assign a comment on product (%d) :\n" +
                "Title : %s\n" +
                "Text : %s",comment.getUserId(),comment.getProduct().getId(),comment.getTitle(),comment.getText());
        Request request = new Request(comment.getUserId(), RequestType.ASSIGN_COMMENT, requestStr, comment);
        RequestManager.getInstance().addRequest(request);
    }

    public boolean doesThisCommentExists(int commentId) {
        Comment comment = DBManager.load(Comment.class,commentId);
        return comment != null;
    }

    public void createScore(String userId, int productId, int score)
            throws NoSuchAProductException {
        Score SCORE = new Score(userId,productId,score);
        ProductManager.getInstance().assignAScore(productId,SCORE);
    }

    public void createSellLog(SubCart subCart, String buyerId, int discount) {
        Product product = subCart.getProduct();
        int moneyGotten = findPrice(subCart);
        User user = DBManager.load(User.class,buyerId);
        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setSourceId(product.getId());
        soldProduct.setSoldPrice(moneyGotten);
        SellLog log = new SellLog(soldProduct,moneyGotten,discount,user,new Date(),DeliveryStatus.DEPENDING);
        DBManager.save(soldProduct);
        SellerManager.getInstance().addASellLog(log,subCart.getSeller());
    }

    public void createPurchaseLog(Cart cart, int discount,Customer customer) {
        List<SoldProductSellerMap> map = new ArrayList<>();
        int pricePaid = 0;
        for (SubCart subCart : cart.getSubCarts()) {
            SoldProduct soldProduct = new SoldProduct();
            soldProduct.setSourceId(subCart.getProduct().getId());
            int price = findPrice(subCart);
            soldProduct.setSoldPrice(price);
            pricePaid += price*subCart.getAmount();
            SoldProductSellerMap toAdd = new SoldProductSellerMap();
            toAdd.setSeller(subCart.getSeller());
            toAdd.setSoldProduct(soldProduct);
            map.add(toAdd);
        }
        PurchaseLog log = new PurchaseLog(new Date(),DeliveryStatus.DEPENDING,map,pricePaid,discount);
        DBManager.save(log);

        CustomerManager.getInstance().addPurchaseLog(log,customer);
    }

    int findPrice(SubCart subCart){
        int price = 0;
        String sellerId = subCart.getSeller().getUsername();
        for (SellerIntegerMap map : subCart.getProduct().getPrices()) {
            if (map.thisIsTheMapKey(sellerId)) {
                price = map.getInteger();
                break;
            }
        }
        return price;
    }

    public Log getLogById(int id){
        Log log = DBManager.load(Log.class,id);
        if (log == null) {
            throw new NoSuchALogException(Integer.toString(id));
        }
        return log;
    }

    public List<User> allBuyers(int productId,Seller seller){
        List<SellLog> logs = seller.getSellLogs();
        List<User> toReturn = new ArrayList<>();
        for (SellLog log : logs) {
            if (log.getProduct().getSourceId() == productId){
                toReturn.add(log.getBuyer());
            }
        }
        return toReturn;
    }

    public void checkIfIsASellerOFProduct(Product product,String username) throws YouAreNotASellerException {
        for (Seller seller : product.getAllSellers()) {
            if (seller.getUsername().equals(username)) return;
        }
        throw new YouAreNotASellerException();
    }

    public boolean boughtThisProduct(int productId, String probableBuyerId) throws NoSuchAProductException {
        Product product = ProductManager.getInstance().findProductById(productId);
        List<User> allBuyers = new ArrayList<>();
        for (Seller seller : product.getAllSellers()) {
            allBuyers.addAll(allBuyers(productId, seller));
        }
        for (User buyer : allBuyers) {
            if (buyer.getUsername().equals(probableBuyerId))
                return true;
        }
        return false;
    }
}
