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
        Manager manager = new Manager(
                info[0],
                info[1],
                info[2],
                info[3],
                info[4],
                info[5],
                new Cart()
        );
        DBManager.save(manager);
    }

    public void deleteUser(String username) {
        User user = DBManager.load(User.class, username);
        DBManager.delete(user);
    }


}
