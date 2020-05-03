package ModelPackage.System;


import ModelPackage.Log.PurchaseLog;
import ModelPackage.Off.DiscountCode;
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
    CartManager cartManager = CartManager.getInstance();

    public List<PurchaseLog> viewOrders(String username){
        Customer customer = (Customer) accountManager.getUserByUsername(username);
        return customer.getPurchaseLogs();
    }

    public HashMap<DiscountCode,Integer> viewDiscountCodes(String username){
        Customer customer = (Customer) accountManager.getUserByUsername(username);
        return customer.getDiscountCodes();
    }

    public void purchase(String username, CustomerInformation customerInformation, DiscountCode discountCode){
        Customer customer = (Customer) accountManager.getUserByUsername(username);

        long totalPrice = getTotalPrice(discountCode, customer);

        long difference = totalPrice - customer.getBalance();

        checkIfCustomerHasEnoughMoney(difference);

        customer.setBalance(customer.getBalance() - totalPrice);
        customer.getCustomerInformation().add(customerInformation);

        // add money to seller
        // create log
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
