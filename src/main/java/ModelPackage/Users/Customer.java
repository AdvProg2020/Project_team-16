package ModelPackage.Users;

import ModelPackage.Log.PurchaseLog;
import ModelPackage.Off.DiscountCode;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class Customer extends User {
    private long balance;
    private List<PurchaseLog> purchaseLogs;
    HashMap<DiscountCode,Integer> discountCodes;

    public Customer(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart, long balance) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
        this.balance = balance;
        this.purchaseLogs = new ArrayList<PurchaseLog>();
        this.discountCodes = new HashMap<DiscountCode, Integer>();
    }
}
