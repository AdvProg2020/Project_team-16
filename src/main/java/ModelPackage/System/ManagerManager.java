package ModelPackage.System;

import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.Request;
import ModelPackage.Users.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ManagerManager {
    private static ManagerManager managerManager = null;
    private ManagerManager(){

    }
    public static ManagerManager getInstance(){
        if (managerManager == null)
            managerManager = new ManagerManager();
        return managerManager;
    }

    AccountManager accountManager = AccountManager.getInstance();

    public void createManagerProfile(String[] info){
        Manager manager = accountManager.createManager(info);
        DBManager.save(manager);
    }

    public void deleteUser(String username) {
        User user = DBManager.load(User.class, username);
        DBManager.delete(user);
    }


}
