package ModelPackage.Users;

import ModelPackage.Log.PurchaseLog;
import ModelPackage.Off.DiscountCode;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class Customer extends User {
    private long balance;
    private List<PurchaseLog> purchaseLogs;
    HashMap<DiscountCode,Integer> discountCodes;

}
