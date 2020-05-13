package controler;

import ModelPackage.System.AccountManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AccountController {
    private AccountManager accountManager = AccountManager.getInstance();
    private User loggedInUser;

    public void usernameInitialCheck(String username){
        if (accountManager.isUsernameAvailable(username)){
            throw new UserNotAvailableException();
        }
    }

    public void createAccount(String[] info, String type){
        accountManager.createAccount(info, type);
    }

    public void login(String username, String password) {
        accountManager.login(username, password);
        loggedInUser = accountManager.getUserByUsername(username);
    }

    public User viewPersonalInfo(){
        return loggedInUser;
    }

    public void editPersonalInfo(String type, String newInfo){
        List<String> info = new ArrayList<>();
        info.add(loggedInUser.getUsername());
        info.add(type);
        info.add(newInfo);

        accountManager.changeInfo((String[]) info.toArray());
    }

    public void logout(){
        accountManager.logout(loggedInUser);
    }
}
