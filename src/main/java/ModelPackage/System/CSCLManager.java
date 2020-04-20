package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.LOG;
import ModelPackage.Log.PurchaseLOG;
import ModelPackage.Log.SellLOG;
import ModelPackage.Product.*;
import ModelPackage.Users.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//@Builder @Data
public class CSCLManager {
    private static CSCLManager csclManager = new CSCLManager();
    private ArrayList<Company> allCompanies;
    private ArrayList<Comment> allComments;
    private ArrayList<Score> allScores;
    private ArrayList<LOG> allLogs;

    private CSCLManager() {
        this.allCompanies = new ArrayList<Company>();
        this.allLogs = new ArrayList<LOG>();
        this.allScores = new ArrayList<Score>();
        this.allComments = new ArrayList<Comment>();
    }

    public static CSCLManager getInstance() {
        return csclManager;
    }
                                /*create Company*/
    public void createCompany(Company newCompany) {
        allCompanies.add(newCompany);
    }
                                /*find company by name*/
    public Company getCompanyByName(String companyName) {
        for (Company company : allCompanies) {
            if (companyName.equals(company.getName()))
                return company;
        }
        return null;
    }
                                 /*edit name of company*/
    public void editCompanyName(String formerName, String newName) {
        getCompanyByName(formerName).setName(newName);
    }
                                /*edit group of company*/
    public void editCompanyGroup(String companyName, String newGroup) {
        getCompanyByName(companyName).setGroup(newGroup);
    }
                                /*create comment*/
    public void createComment(String productId, String userId, String title, String text, boolean hasBoughtProduct) {
        allComments.add(new Comment(productId, userId, title, text, CommentStatus.VERIFIED, hasBoughtProduct));
    }
                                /*create score*/
    public void createScore(String userId, String productId, int score) {
        allScores.add(new Score(userId, productId, score));
    }
                                /*create sellLog*/
    public void createSellLog(String[] ids, int[] numbers, Date dateAdded, DeliveryStatus deliveryStatus) {
        String productId = ids[0];
        String userId = ids[1];
        int moneyGotten = numbers[0];
        int discount = numbers[1];
        LOG log = new SellLOG(ProductManager.findProductById(productId), moneyGotten, discount,
                AccountManager.getUserByName(userId), dateAdded, deliveryStatus);
        allLogs.add(log);
    }
                                /*create purchase log*/
    public void createPurchaseLog(int[] prices, Date dateAdded, DeliveryStatus deliveryStatus, HashMap<Product, Integer> productsAndItsPrices, HashMap<Seller, Integer> sellers) {
        int pricePaid = prices[0];
        int discount = prices[1];
        LOG log = new PurchaseLOG(dateAdded, deliveryStatus, productsAndItsPrices,
                pricePaid, discount, sellers);
        allLogs.add(log);
    }
}
