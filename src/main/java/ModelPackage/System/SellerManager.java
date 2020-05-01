package ModelPackage.System;


import ModelPackage.Log.SellLog;
import ModelPackage.Product.Company;
import ModelPackage.Users.Seller;

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
    CSCLManager csclManager = CSCLManager.getInstance();


    public Company viewCompanyInformation(String username) {
        Seller seller = (Seller) accountManager.getUserByUsername(username);
        return seller.getCompany();
    }

    public List<SellLog> viewSalesHistory(String username){
        Seller seller = (Seller) accountManager.getUserByUsername(username);
        return seller.getSellLogs();
    }




}
