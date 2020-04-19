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

    public void changeInfo(User user, String info, String newInfo){}

    public void logout(User user){}

    public User getUserByName(String username){
        return null;
    }

    private boolean isCorrectPassword(String password,String username){
        return false;
    }


}
