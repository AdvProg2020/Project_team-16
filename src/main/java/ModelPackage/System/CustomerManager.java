package ModelPackage.System;


public class CustomerManager {
    private static CustomerManager customerManager = null;
    private CustomerManager(){

    }
    public static CustomerManager getInstance(){
        if (customerManager == null)
            customerManager = new CustomerManager();
        return customerManager;
    }
}
