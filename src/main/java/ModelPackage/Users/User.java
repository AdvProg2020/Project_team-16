package ModelPackage.Users;


import lombok.*;

@Data
@NoArgsConstructor
public class User {
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected Cart cart;
}
