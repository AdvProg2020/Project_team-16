package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.Log;
import ModelPackage.Log.PurchaseLog;
import ModelPackage.Log.SellLog;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.*;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import ModelPackage.Users.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Data
public class CSCLManager {
    private static CSCLManager csclManager = new CSCLManager();
    private ArrayList<Company> allCompanies;
    private ArrayList<Comment> allComments;
    private ArrayList<Score> allScores;
    private ArrayList<Log> allLogs;

    private CSCLManager() {
        this.allCompanies = new ArrayList<>();
        this.allLogs = new ArrayList<>();
        this.allScores = new ArrayList<>();
        this.allComments = new ArrayList<>();
    }

    public static CSCLManager getInstance() {
        return csclManager;
    }

    public void createCompany(Company newCompany) {
        allCompanies.add(newCompany);
    }

    public Company getCompanyByName(String companyName) {
        for (Company company : allCompanies) {
            if (companyName.equals(company.getName()))
                return company;
        }
        return null;
    }

    public boolean doesCompanyExist(String companyName) {
        for (Company company : allCompanies) {
            if (companyName.equals(company.getName()))
                return true;
        }
        return false;
    }

    public void editCompanyName(String formerName, String newName) {
        getCompanyByName(formerName).setName(newName);
    }

    public void editCompanyGroup(String companyName, String newGroup) {
        getCompanyByName(companyName).setGroup(newGroup);
    }

    public void addProductToCompany(String productId, String companyName) {
        getCompanyByName(companyName).getProductsIn().add(ProductManager.getInstance().findProductById(productId));
    }

    public void removeProductFromCompany(String productId, String companyName) {
        getCompanyByName(companyName).getProductsIn().remove(ProductManager.getInstance().findProductById(productId));
    }

    public void createComment(String[] data, boolean hasBoughtProduct) {
        String productId = data[0];
        String userId = data[1];
        String title = data[2];
        String text = data[3];
        allComments.add(new Comment(productId, userId, title, text, CommentStatus.VERIFIED, hasBoughtProduct));
    }

    public void createScore(String userId, String productId, int score) {
        allScores.add(new Score(userId, productId, score));
    }

    /*public void createSellLog(String[] ids, int[] numbers, Date dateAdded, DeliveryStatus deliveryStatus) {
        String productId = ids[0];
        String userId = ids[1];
        int moneyGotten = numbers[0];
        int discount = numbers[1];
        Log log = new SellLog(ProductManager.getInstance().findProductById(productId), moneyGotten, discount,
                AccountManager.getUserByName(userId), dateAdded, deliveryStatus);
        allLogs.add(log);
    }*/

    /*public void createPurchaseLog(Cart cart, Date dateAdded, String userId) {
        HashMap<Product, Integer> productsAndTheirPrices = new HashMap<>();
        int discount = 0;
        for (SubCart subCart : cart.getSubCarts()) {
            productsAndTheirPrices.put(subCart.getProduct(), subCart.getAmount() * subCart.getProduct().getPrices().
                    get(subCart.getSellerId()));
        }
        HashMap<String, String> sellersAndTheirProducts = new HashMap<>();
        for (SubCart subCart : cart.getSubCarts()) {
            sellersAndTheirProducts.put(subCart.getSellerId(), subCart.getProductId());
        }
        for (DiscountCode discountCode : DiscountManager.getInstance().getDiscountCodes()) {
            for (User user : discountCode.getUsers().keySet()) {
                if (user.getUsername().equals(userId)) {
                    discount = discountCode.getOffPercentage();
                    break;
                }
            }
        }
        int pricePaid =(int)(cart.getTotalPrice() - (double)discount / 100 * cart.getTotalPrice());
        allLogs.add(new PurchaseLog(dateAdded, DeliveryStatus.DEOENDING, productsAndTheirPrices, pricePaid, discount, sellersAndTheirProducts));
    }*/
}
