package ModelPackage.Users;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_manager")
public class Manager extends User {
    public Manager(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
    }

    Manager(){

    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
