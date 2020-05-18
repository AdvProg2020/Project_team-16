package ModelPackage.Users;

import ModelPackage.Log.PurchaseLog;
import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Off.DiscountCode;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter @Getter
@Entity @Table(name = "t_customer")
public class Customer extends User {
    @Column(name = "BALANCE")
    private long balance;

    @ElementCollection(targetClass = CustomerInformation.class)
        @OneToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "t_customer_info_map")
    private List<CustomerInformation> customerInformation;

    @ElementCollection(targetClass = PurchaseLog.class)
        @OneToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "t_customer_purchase_map")
    private List<PurchaseLog> purchaseLogs;

    @ElementCollection
            @OneToMany(cascade = CascadeType.ALL)
            @JoinTable(name = "t_customer_discount_map")
    private List<DiscountcodeIntegerMap> discountCodes;

    public Customer(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart, long balance) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
        this.balance = balance;
        this.customerInformation = new ArrayList<>();
        this.purchaseLogs = new ArrayList<PurchaseLog>();
        this.discountCodes = new ArrayList<>();
    }

    public Customer(){
        this.customerInformation = new ArrayList<>();
        this.purchaseLogs = new ArrayList<PurchaseLog>();
        this.discountCodes = new ArrayList<>();
    }
}
