package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.SellLog;
import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Company;
import ModelPackage.Product.Score;
import lombok.Builder;
import lombok.Data;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Date;

//@Builder @Data
public class CSCLManager {
    private static CSCLManager csclManager = new CSCLManager();
    private ArrayList<Company> allCompanies;
    private ArrayList<Comment> allComments;
    private ArrayList<Score> allScores;
    private ArrayList<Log> allLogs;

    private CSCLManager() {
        this.allCompanies = new ArrayList<Company>();
        this.allLogs = new ArrayList<Log>();
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
        Log log = new SellLog(ProductManager.findProductById(productId), moneyGotten, discount,
                AccountManager.getUserByName(userId), dateAdded, deliveryStatus);
        allLogs.add(log);
    }
}
