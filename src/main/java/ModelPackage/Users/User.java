package ModelPackage.Users;


import lombok.*;

import javax.persistence.*;

@Data @Entity
@Table(name = "t_user")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id @Column(name = "username" , unique = true, length = 64 , nullable = false)
    protected String username;

    @Column(name = "PASSWORD" , length = 64)
    protected String password;

    @Column(name = "FIRST_NAME")
    protected String firstName;

    @Column(name = "LAST_NAME")
    protected String lastName;

    @Column(name = "EMAIL")
    protected String email;

    @Column(name = "PHONE_NUMBER")
    protected String phoneNumber;

    @OneToOne
        @JoinColumn(name = "CART")
    protected Cart cart;

    @Transient
    private boolean hasSignedIn;

    public User(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cart = cart;
        this.hasSignedIn = false;
    }
}
