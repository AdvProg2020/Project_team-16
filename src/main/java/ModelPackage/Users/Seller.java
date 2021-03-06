package ModelPackage.Users;

import ModelPackage.Log.SellLog;
import ModelPackage.Off.Off;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
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

    @ElementCollection(targetClass = Off.class)
        @OneToMany
    private List<Off> offs;

    @ElementCollection(targetClass = SellLog.class)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellLog> sellLogs;

    @OneToMany
    private List<SellPackage> packages;

    @Column
    private boolean verified;

    @ElementCollection
    @OneToMany
    private List<Request> requests;

    public Seller(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart, Company company, long balance) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
        this.company = company;
        this.balance = balance;
        this.offs = new ArrayList<>();
        this.sellLogs = new ArrayList<>();
        this.packages = new ArrayList<>();
        this.requests = new ArrayList<>();
    }
    public Seller(){
        this.offs = new ArrayList<>();
        this.sellLogs = new ArrayList<>();
        this.packages = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

    public boolean getVerified(){
        return verified;
    }

    public boolean equals(Seller seller) {
        return this.username.equals(seller.username);
    }

    public void addRequest(Request request) {
        DBManager.save(request);
        requests.add(request);
        DBManager.save(this);
    }

    public SellPackage findPackageByProductId(int id) throws NoSuchAPackageException {
        for (SellPackage aPackage : packages) {
            if (aPackage.isForProductWithId(id)) return aPackage;
        }
        throw new NoSuchAPackageException();
    }
}
