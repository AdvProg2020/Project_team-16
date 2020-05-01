package ModelPackage.System;


import ModelPackage.Product.Company;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;

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


    public Company viewCompanyInformation(String username) throws UserNotAvailableException {
        Seller seller = (Seller) accountManager.getUserByUsername(username);
        return seller.getCompany();
    }




}
