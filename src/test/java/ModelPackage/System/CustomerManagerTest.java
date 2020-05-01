package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.PurchaseLog;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CustomerManagerTest {
    CustomerManager customerManager;
    Customer hatam;
    PurchaseLog dullForKimmiLog;
    Product dullForKimmi;
    Seller marmof;
    Company adidas;
    List<PurchaseLog> purchaseLogs;

    {
        customerManager = CustomerManager.getInstance();

        hatam = new Customer(
                "hatam008",
                "hatam009",
                "Ali",
                "Hatami",
                "hatam008@gmail.com",
                "0912 133 1232",
                new Cart(),
                10000000
        );

        adidas = new Company("Adidas","115", "Clothing",new ArrayList<>());
        marmof = new Seller(
                "marmofayezi",
                "marmof.ir",
                "Cyrus",
                "Statham",
                "marmof@gmail.com",
                "+1 992 1122",
                new Cart(),
                adidas,
                10000000
        );
        dullForKimmi = new Product();

        HashMap<String,String> productSellerHashMap = new HashMap<>();
        productSellerHashMap.put(dullForKimmi.getProductId(),marmof.getUsername());

        dullForKimmiLog = new PurchaseLog(
                new Date(),
                DeliveryStatus.DELIVERED,
                productSellerHashMap,
                10000,
                9
        );

        purchaseLogs = new ArrayList<>();
        purchaseLogs.add(dullForKimmiLog);

        hatam.setPurchaseLogs(purchaseLogs);
    }

    @Test
    public void viewOrders(){
        new MockUp<AccountManager>(){
            @Mock
            User getUserByUsername(String username){
                return hatam;
            }
        };

        List<PurchaseLog> actualPurchaseLog = customerManager.viewOrders("hatam008");

        Assert.assertArrayEquals(purchaseLogs.toArray(),actualPurchaseLog.toArray());
    }



}
