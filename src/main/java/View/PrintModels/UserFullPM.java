package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserFullPM {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;

    public UserFullPM(String username, String firstName, String lastName, String email, String phoneNumber, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    @Override
    public String toString() {
        return username;
    }
}
