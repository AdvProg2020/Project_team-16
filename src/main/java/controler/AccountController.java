package controler;

import ModelPackage.System.AccountManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.account.WrongPasswordException;

public class AccountController {
    private AccountManager accountManager = AccountManager.getInstance();


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
    }
}
