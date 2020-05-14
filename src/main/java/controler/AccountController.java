package controler;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.User;
import View.PrintModels.UserFullPM;

import java.util.ArrayList;
import java.util.List;

public class AccountController extends Controller {
    private User loggedInUser;

    public void usernameInitialCheck(String username){
        if (accountManager.isUsernameAvailable(username)){
            throw new UserNotAvailableException();
        }
    }

    public void createAccount(String[] info, String type){
        accountManager.createAccount(info, type);
    }

    public String login(String username, String password) {
        String type = accountManager.login(username, password);
        loggedInUser = accountManager.getUserByUsername(username);
        return type;
    }

    public UserFullPM viewPersonalInfo(){
        return new UserFullPM(
                loggedInUser.getUsername(),
                loggedInUser.getFirstName(),
                loggedInUser.getLastName(),
                loggedInUser.getEmail(),
                loggedInUser.getPhoneNumber(),
                loggedInUser.getClass().toString().split(" ")[1]
        );
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
