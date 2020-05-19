package ModelPackage.Users;

import ModelPackage.Log.SellLog;
import ModelPackage.Off.Off;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter @Getter
@Entity @Table(name = "t_seller")
public class Seller extends User {
    @ManyToOne
        @JoinColumn(name = "COMPANY")
    private Company company;

    @Column(name = "BALANCE")
    private long balance;

    @ElementCollection(targetClass = Product.class)
        @OneToMany
    private List<Product> products;

    @ElementCollection(targetClass = Off.class)
        @OneToMany
    private List<Off> offs;

    @ElementCollection(targetClass = SellLog.class)
        @OneToMany(cascade = CascadeType.ALL)
    private List<SellLog> sellLogs;


    private boolean verified;

    public Seller(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart, Company company, long balance) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
        this.company = company;
        this.balance = balance;
        this.offs = new ArrayList<Off>();
        this.products = new ArrayList<>();
        this.sellLogs = new ArrayList<SellLog>();
    }
    public Seller(){
        this.offs = new ArrayList<Off>();
        this.products = new ArrayList<>();
        this.sellLogs = new ArrayList<SellLog>();
    }

    public boolean getVerified(){
        return verified;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
