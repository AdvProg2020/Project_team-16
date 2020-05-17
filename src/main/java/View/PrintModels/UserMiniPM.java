package View.PrintModels;

import lombok.Data;

@Data
public class UserMiniPM {
    private String username;
    private String role;

    public UserMiniPM(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
