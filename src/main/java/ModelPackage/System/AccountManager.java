package ModelPackage.System;

import ModelPackage.Users.User;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class AccountManager {
    @Getter private static List<User> users = new ArrayList<>();
    private static AccountManager accountManager = null;
    private AccountManager(){}
    public static AccountManager getInstance(){
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }

    public void createAccount(User user){}

    public void login(User user){}

    public void changeInfo(String username, String info, String newInfo){
        User user = getUserByUsername(username);
        switch (info){
            case "password" : user.setPassword(newInfo); break;
            case "firstName" : user.setFirstName(newInfo); break;
            case "lastName" : user.setLastName(newInfo); break;
            case "email" : user.setEmail(newInfo); break;
            case "phoneNumber" : user.setPhoneNumber(newInfo); break;
        }
    }

    public void logout(User user){}

    private boolean isCorrectPassword(String password,String username){
        return false;
    }

    private User getUserByUsername(String username){
        for (User user : users) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }


}
