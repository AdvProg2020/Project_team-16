package ModelPackage.Users;

import ModelPackage.Log.PurchaseLOG;
import ModelPackage.Off.DiscountCode;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Setter @Getter
public class Customer extends User {
    private long balance;
    private List<PurchaseLOG> purchaseLogs;
    HashMap<DiscountCode,Integer> discountCodes;

    public Customer(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart, long balance) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
        this.balance = balance;
        this.purchaseLogs = new ArrayList<PurchaseLOG>();
        this.discountCodes = new HashMap<DiscountCode, Integer>();
    }
}
