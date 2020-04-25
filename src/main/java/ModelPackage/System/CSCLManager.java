package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.Log;
import ModelPackage.Log.PurchaseLog;
import ModelPackage.Log.SellLog;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.*;
import ModelPackage.Users.*;
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

    public void createComment(Comment comment) {
        String requestStr = String.format("%s has requested to create comment with title %s and text %s \n with id " +
                        "%s on product with id %s",
                comment.getUserId(), comment.getTitle(), comment.getText(), comment.getId(), comment.getProductId());
        Request request = new Request(comment.getUserId(), RequestType.CREATE_COMMENT, requestStr, comment);
        RequestManager.getInstance().addRequest(request);
    }

    public void addCommentToList(Comment comment) {
        allComments.add(comment);
    }

    public boolean doesThisCommentExists(String commentId) {
        for (Comment comment : allComments) {
            if (commentId.equals(comment.getId()))
                return true;
        }
        return false;
    }

    public void createScore(String userId, String productId, int score) {
        allScores.add(new Score(userId, productId, score));
    }

    /*public void createSellLog(SubCart subCart, String buyerId, int discount) {
        Product product = subCart.getProduct();
        int moneyGotten = 0;
        for (String id : product.getPrices().keySet()) {
            if (id.equals(subCart.getSellerId())) {
                moneyGotten = product.getPrices().get(id) * subCart.getAmount();
                break;
            }
        }
        allLogs.add(new SellLog(product, moneyGotten, discount, AccountManager.getUserByName(buyerId),
                new Date(), DeliveryStatus.DEOENDING));
    }*/

    public void createPurchaseLog(Cart cart, int discount) {
        HashMap<String, String> productsAndTheirSellers = new HashMap<>();
        for (SubCart subCart : cart.getSubCarts()) {
            productsAndTheirSellers.put(subCart.getProductId(), subCart.getSellerId());
        }
        int pricePaid =(int)(cart.getTotalPrice() - (double)discount / 100 * cart.getTotalPrice());
        allLogs.add(new PurchaseLog(new Date(), DeliveryStatus.DEOENDING, productsAndTheirSellers, pricePaid, discount));
    }
}
