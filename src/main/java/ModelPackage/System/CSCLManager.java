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

    public void createSellLog(Cart cart, String sellerId, String buyerId) {
        ArrayList<Product> soldProducts = new ArrayList<>();
        for (SubCart subCart : cart.getSubCarts()) {
            if (subCart.getSellerId().equals(sellerId))
                soldProducts.add(subCart.getProduct());
        }
        // TODO : building calculateProductsOffs method in SellerManager
        int offPrice = SellerManager.getInstance().calculateProductsOffs(soldProducts,
                AccountManager.getInstance().getUserByName(sellerId));
        // TODO : building calculatePriceOfSeller method in ProductManager like the code commented below
        int gottenPrice = ProductManager.getInstance().calculatePriceOfSeller(soldProducts, sellerId);
        allLogs.add(new SellLog(soldProducts, gottenPrice, offPrice,
                        AccountManager.getInstance().getUserByName(buyerId), new Date(),
                DeliveryStatus.DEOENDING));
        /*for (Product product : soldProducts) {
            for (String id : product.getPrices().keySet()) {
                if (id.equals(sellerId)) {
                    gottenPrice += product.getPrices().get(id);
                    break;
                }
            }
        }*/
    }

    public void createPurchaseLog(Cart cart, DiscountCode discountCode) {
        HashMap<String, String> productsAndTheirSellers = new HashMap<>();
        int discount = discountCode.getOffPercentage();
        for (SubCart subCart : cart.getSubCarts()) {
            productsAndTheirSellers.put(subCart.getProductId(), subCart.getSellerId());
        }
                    // discountCode should be passed to this method
        /*for (DiscountCode discountCode : DiscountManager.getInstance().getDiscountCodes()) {
            for (User user : discountCode.getUsers().keySet()) {
                if (user.getUsername().equals(userId)) {
                    discount = discountCode.getOffPercentage();
                    break;
                }
            }
        }*/
        int pricePaid =(int)(cart.getTotalPrice() - (double)discount / 100 * cart.getTotalPrice());
        allLogs.add(new PurchaseLog(new Date(), DeliveryStatus.DEOENDING, productsAndTheirSellers, pricePaid, discount));
    }
}
