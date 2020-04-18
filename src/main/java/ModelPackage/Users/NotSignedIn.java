package ModelPackage.Users;

public class NotSignedIn extends User {
    public NotSignedIn(String username, String password, String firstName, String lastName, String email, String phoneNumber, Cart cart) {
        super(username, password, firstName, lastName, email, phoneNumber, cart);
    }
}
