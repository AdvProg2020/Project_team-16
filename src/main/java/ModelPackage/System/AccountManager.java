package ModelPackage.System;

import ModelPackage.Product.Company;
import ModelPackage.Users.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class AccountManager {
    @Getter private static List<User> users = new ArrayList<>();
    private static AccountManager accountManager = null;
    private AccountManager(){}
    public static AccountManager getInstance(){
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }
    private CSCLManager csclManager = CSCLManager.getInstance();

    public void createAccount(String[] info, String type){
        User user = null;
        switch (type){
            case "seller" : user = createSeller(info); break;
            case "manager" : user = createManager(info); break;
            case  "customer" : user = createCustomer(info); break;
        }
        users.add(user);
    }

    private Seller createSeller(String[] info){
        return new Seller(info[0],info[1],info[2],info[3],info[4],info[5],new Cart(),csclManager.getCompanyByName(info[6]),Long.parseLong(info[7]));
    }

    private Manager createManager(String[] info){
        return new Manager(info[0],info[1],info[2],info[3],info[4],info[5],new Cart());
    }

    private Customer createCustomer(String[] info){
        return new Customer(info[0],info[1],info[2],info[3],info[4],info[5],new Cart(),Long.parseLong(info[6]));
    }

    public boolean login(String username,String password){
        return isCorrectPassword(username, password);
    }

    public void changeInfo(String username, String info, String newInfo){
        User user = getUserByUsername(username);
        switch (info){
            case "password" : user.setPassword(newInfo); break;
            case "firstName" : user.setFirstName(newInfo); break;
            case "lastName" : user.setLastName(newInfo); break;
            case "email" : user.setEmail(newInfo); break;
            case "phoneNumber" : user.setPhoneNumber(newInfo); break;
        }
    }

    public void logout(String username){ }

    private boolean isCorrectPassword(String username,String password){
        return getUserByUsername(username).getPassword().equals(password);
    }

    private User getUserByUsername(String username){
        for (User user : users) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public boolean isUsernameAvailable(String username){
        for (User user : users) {
            if (user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }


}
