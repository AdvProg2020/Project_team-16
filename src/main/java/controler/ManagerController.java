package controler;

import ModelPackage.System.AccountManager;
import ModelPackage.System.database.DBManager;
import ModelPackage.Users.User;

import java.util.List;

public class ManagerController {
    private AccountManager accountManager = AccountManager.getInstance();

    public List<User> manageUsers (){
       return DBManager.loadAllData(User.class);
    }


}
