package ModelPackage.System;


import ModelPackage.Log.PurchaseLog;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Users.Customer;

import java.util.HashMap;
import java.util.List;

public class CustomerManager {
    private static CustomerManager customerManager = null;
    private CustomerManager(){

    }
    public static CustomerManager getInstance(){
        if (customerManager == null)
            customerManager = new CustomerManager();
        return customerManager;
    }

    AccountManager accountManager = AccountManager.getInstance();

    public List<PurchaseLog> viewOrders(String username){
        Customer customer = (Customer) accountManager.getUserByUsername(username);
        return customer.getPurchaseLogs();
    }

    public HashMap<DiscountCode,Integer> viewDiscountCodes(String username){
        Customer customer = (Customer) accountManager.getUserByUsername(username);
        return customer.getDiscountCodes();
    }
}
