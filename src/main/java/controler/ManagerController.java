package controler;

import ModelPackage.System.AccountManager;
import ModelPackage.System.database.DBManager;
import ModelPackage.Users.User;

import java.util.List;

public class ManagerController extends Controller {

    public List<User> manageUsers (){
       return DBManager.loadAllData(User.class);
    }

    public User viewUser(String username){
        return accountManager.getUserByUsername(username);
    }

    public void deleteUser(String username){

    }

}
