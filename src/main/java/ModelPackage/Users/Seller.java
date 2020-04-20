package ModelPackage.Users;

import ModelPackage.Log.SellLOG;
import ModelPackage.Off.Off;
import ModelPackage.Product.Company;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
public class Seller extends User {
    private Company company;
    private long balance;
    private List<String> productIds;
    private List<Off> offs;
    private List<SellLOG> sellLogs;

    public Seller(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart, Company company, long balance) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
        this.company = company;
        this.balance = balance;
        this.offs = new ArrayList<Off>();
        this.productIds = new ArrayList<String>();
        this.sellLogs = new ArrayList<SellLOG>();
    }
}
