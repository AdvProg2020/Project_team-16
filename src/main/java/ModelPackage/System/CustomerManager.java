package ModelPackage.System;


import ModelPackage.Log.PurchaseLog;
import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.NotEnoughMoneyException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.CustomerInformation;

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
    SellerManager sellerManager = SellerManager.getInstance();
    CSCLManager csclManager = CSCLManager.getInstance();

    public List<PurchaseLog> viewOrders(String username){
        Customer customer = DBManager.load(Customer.class, username);
        return customer.getPurchaseLogs();
    }

    public List<DiscountcodeIntegerMap> viewDiscountCodes(String username){
        Customer customer = DBManager.load(Customer.class, username);
        return customer.getDiscountCodes();
    }

    public void purchase(String username, CustomerInformation customerInformation, DiscountCode discountCode){
        Customer customer = DBManager.load(Customer.class, username);

        purchaseForCustomer(customer, customerInformation, discountCode);

        sellerManager.getMoneyFromSale(customer.getCart());

        if (discountCode != null) {
            csclManager.createPurchaseLog(customer.getCart(), discountCode.getOffPercentage());
        } else {
            csclManager.createPurchaseLog(customer.getCart(), 0);
        }
    }

    public void purchaseForCustomer(Customer customer, CustomerInformation customerInformation, DiscountCode discountCode) {
        long totalPrice = getTotalPrice(discountCode, customer);

        long difference = totalPrice - customer.getBalance();

        checkIfCustomerHasEnoughMoney(difference);

        customer.setBalance(customer.getBalance() - totalPrice);
        customer.getCustomerInformation().add(customerInformation);
    }

    public long getTotalPrice(DiscountCode discountCode, Customer customer) {
        Cart cart = customer.getCart();
        long totalPrice;

        if (discountCode != null){
            long discount = cart.getTotalPrice() * discountCode.getOffPercentage() / 100;
            if (discount > discountCode.getMaxDiscount()){
                totalPrice = cart.getTotalPrice() - discountCode.getMaxDiscount();
            } else {
                totalPrice = cart.getTotalPrice() - discount;
            }
        } else {
            totalPrice = cart.getTotalPrice();
        }
        return totalPrice;
    }

    public void checkIfCustomerHasEnoughMoney(long difference){
        if (difference > 0){
            throw new NotEnoughMoneyException(difference);
        }
    }
}
