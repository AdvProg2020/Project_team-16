package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserPM {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String shippingAddress;
    private String role;
}
