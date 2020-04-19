package ModelPackage.System;

import ModelPackage.Users.User;

import java.util.List;

public class AccountManager {
    private static List<User> users;
    private static AccountManager accountManager = null;
    private AccountManager(){}
    public static AccountManager getInstance(){
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }

    public void createAccount(User user){}

    public void login(User user){}

    public void changeInfo(User user, String info, String newInfo){
        switch (info){
            case "password" : user.setPassword(newInfo); break;
            case "firstName" : user.setFirstName(newInfo); break;
            case "lastName" : user.setLastName(newInfo); break;
            case "email" : user.setEmail(newInfo); break;
            case "phoneNumber" : user.setPhoneNumber(newInfo); break;
        }
    }

    public void logout(User user){}

    public User getUserByName(String username){
        return null;
    }

    private boolean isCorrectPassword(String password,String username){
        return false;
    }


}
