package controler;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.User;
import View.PrintModels.UserFullPM;

import java.util.ArrayList;
import java.util.List;

public class AccountController extends Controller {

    public void usernameInitialCheck(String username){
        if (accountManager.isUsernameAvailable(username)){
            throw new UserNotAvailableException();
        }
    }

    public void createAccount(String[] info, String type){
        accountManager.createAccount(info, type);
    }

    public String login(String username, String password) {
        return accountManager.login(username, password);
    }

    public UserFullPM viewPersonalInfo(String username){
        User user = accountManager.viewPersonalInfo(username);
        return new UserFullPM(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getClass().getName()
        );
    }

    public void editPersonalInfo(String username, String type, String newInfo){
        List<String> info = new ArrayList<>();
        info.add(username);
        info.add(type);
        info.add(newInfo);

        accountManager.changeInfo((String[]) info.toArray());
    }

    public void logout(String username){
        accountManager.logout(username);
    }
}
