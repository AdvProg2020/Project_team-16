package ModelPackage.Users;


import ModelPackage.Cart;
import lombok.*;

@Data
@NoArgsConstructor
@Builder
public class User {
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected Cart cart;
}
