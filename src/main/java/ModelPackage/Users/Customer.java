package ModelPackage.Users;

import ModelPackage.Log.PurchaseLog;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class Customer {
    private long balance;
    private List<PurchaseLog> purchaseLogs;
    HashMap<DiscountCode,Integer> discountCodes;

}
