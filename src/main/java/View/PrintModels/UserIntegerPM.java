package View.PrintModels;

import lombok.Data;

@Data
public class UserIntegerPM {
    private String username;
    private int integer;

    public UserIntegerPM(String username, int integer) {
        this.username = username;
        this.integer = integer;
    }
}
