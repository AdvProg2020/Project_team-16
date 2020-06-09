package ModelPackage.System;


import ModelPackage.Log.PurchaseLog;
import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.NotEnoughMoneyException;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.CustomerInformation;
import ModelPackage.Users.SubCart;

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

    private SellerManager sellerManager = SellerManager.getInstance();
    private CSCLManager csclManager = CSCLManager.getInstance();

    public List<PurchaseLog> viewOrders(String username){
        Customer customer = DBManager.load(Customer.class, username);
        return customer.getPurchaseLogs();
    }

    public List<DiscountcodeIntegerMap> viewDiscountCodes(String username){
        Customer customer = DBManager.load(Customer.class, username);
        return customer.getDiscountCodes();
    }

    public void purchase(String username, CustomerInformation customerInformation, DiscountCode discountCode)
            throws NotEnoughAmountOfProductException, NoSuchAProductException {
        Customer customer = DBManager.load(Customer.class, username);

        checkIfThereIsEnoughAmount(customer);

        purchaseForCustomer(customer, customerInformation, discountCode);

        sellerManager.getMoneyFromSale(customer.getCart());

        if (discountCode != null) {
            csclManager.createPurchaseLog(customer.getCart(), discountCode.getOffPercentage(),customer);
        } else {
            csclManager.createPurchaseLog(customer.getCart(), 0,customer);
        }

        productChangeInPurchase(customer);

        DBManager.save(customer);
    }

    public void checkIfThereIsEnoughAmount(Customer customer) throws NotEnoughAmountOfProductException, NoSuchAProductException {
        Cart cart = customer.getCart();

        for (SubCart subCart : cart.getSubCarts()) {
            CartManager.getInstance().checkIfThereIsEnoughAmountOfProduct(
                    subCart.getProduct().getId(),
                    subCart.getSeller().getUsername(),
                    subCart.getAmount()
            );
        }
    }

    public void purchaseForCustomer(Customer customer, CustomerInformation customerInformation, DiscountCode discountCode) {
        long totalPrice = getTotalPrice(discountCode, customer);

        long difference = totalPrice - customer.getBalance();

        checkIfCustomerHasEnoughMoney(difference);

        customer.setBalance(customer.getBalance() - totalPrice);
        customer.getCustomerInformation().add(customerInformation);
    }

    public void productChangeInPurchase(Customer customer){
        Cart cart = customer.getCart();

        for (SubCart subCart : cart.getSubCarts()) {
            ProductManager.getInstance().changeAmountOfStock(
                    subCart.getProduct().getId(),
                    subCart.getSeller().getUsername(),
                    -subCart.getAmount()
            );
        }
    }

    public long getTotalPrice(DiscountCode discountCode, Customer customer) {
        Cart cart = customer.getCart();
        long totalPrice;

        if (discountCode != null){
            double discount = (double) cart.getTotalPrice() * discountCode.getOffPercentage() / 100;
            if (discount > discountCode.getMaxDiscount()){
                totalPrice = cart.getTotalPrice() - discountCode.getMaxDiscount();
            } else {
                totalPrice = cart.getTotalPrice() - (int)discount;
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

    public void addPurchaseLog(PurchaseLog log, Customer customer){
        customer.getPurchaseLogs().add(log);
        DBManager.save(customer);
    }
}
