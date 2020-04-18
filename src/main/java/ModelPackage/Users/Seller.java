package ModelPackage.Users;

import ModelPackage.Log.SellLog;
import ModelPackage.Product.Company;
import lombok.*;

import java.util.List;

@Data
@Builder
public class Seller extends User {
    private Company company;
    private long balance;
    private List<String> productIds;
    private List<Off> offs;
    private List<SellLog> sellLogs;
}
