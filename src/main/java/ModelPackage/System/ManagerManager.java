package ModelPackage.System;

import ModelPackage.System.exeption.account.UserNotAvailableException;
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
        this.managers = new ArrayList<>();
    }
    public static ManagerManager getInstance(){
        if (managerManager == null)
            managerManager = new ManagerManager();
        return managerManager;
    }

    private List<Request> allRequests;
    private List<Manager> managers;
    AccountManager accountManager = AccountManager.getInstance();

    public void createManagerProfile(String[] info){
        Manager manager = accountManager.createManager(info);
        managers.add(manager);
        accountManager.getUsers().add(manager);
    }

    public void deleteUser(String username) throws UserNotAvailableException {
        User user = accountManager.getUserByUsername(username);
        accountManager.getUsers().remove(user);
    }


}
