package controler;

import ModelPackage.System.AccountManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;

public class AccountController {
    private AccountManager accountManager = AccountManager.getInstance();


    public void createAccountInitialCheck(String username){
        if (accountManager.isUsernameAvailable(username)){
            throw new UserNotAvailableException();
        }
    }

    public void createAccount(String[] info, String type){
        accountManager.createAccount(info, type);
    }


}
