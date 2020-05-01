package ModelPackage.System;


import ModelPackage.Log.SellLog;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Users.Seller;

import java.util.ArrayList;
import java.util.List;

public class SellerManager {
    private static SellerManager sellerManager = null;
    private SellerManager(){    }
    public static SellerManager getInstance(){
        if (sellerManager == null)
            sellerManager = new SellerManager();
        return sellerManager;
    }

    AccountManager accountManager = AccountManager.getInstance();
    ProductManager productManager = ProductManager.getInstance();
    CSCLManager csclManager = CSCLManager.getInstance();


    public Company viewCompanyInformation(String username) {
        Seller seller = (Seller) accountManager.getUserByUsername(username);
        return seller.getCompany();
    }

    public List<SellLog> viewSalesHistory(String username){
        Seller seller = (Seller) accountManager.getUserByUsername(username);
        return seller.getSellLogs();
    }

    public List<Product> viewProducts(String username){
        Seller seller = (Seller) accountManager.getUserByUsername(username);

        ArrayList<String> productIds = (ArrayList<String>) seller.getProductIds();
        ArrayList<Product> products = new ArrayList<>();

        for (String id : productIds) {
            Product product = productManager.findProductById(id);
            products.add(product);
        }

        return products;
    }




}
