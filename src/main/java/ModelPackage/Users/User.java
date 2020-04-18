package ModelPackage.Users;


import lombok.*;

@Data
public class User {
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected Cart cart;

    public User(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cart = cart;
    }
}
