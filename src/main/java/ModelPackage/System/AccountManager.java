package ModelPackage.System;

import ModelPackage.System.exeption.account.SameInfoException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.account.WrongPasswordException;
import ModelPackage.Users.*;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AccountManager {
    private static AccountManager accountManager = null;
    private AccountManager(){
        this.users = new ArrayList<>();
    }
    public static AccountManager getInstance(){
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }

    private List<User> users;
    private RequestManager requestManager = RequestManager.getInstance();
    private CSCLManager csclManager = CSCLManager.getInstance();

    public void createAccount(String[] info, String type){
        User user = null;
        switch (type){
            case "seller" : createSeller(info); break;
            case "manager" : user = createManager(info); break;
            case  "customer" : user = createCustomer(info); break;
        }
        users.add(user);
    }

    private void createSeller(String[] info){
        Seller seller = new Seller(
                info[0],
                info[1],
                info[2],
                info[3],
                info[4],
                info[5],
                new Cart(),
                csclManager.getCompanyByName(info[6]),
                Long.parseLong(info[7]));
        String requestStr = String.format("%s has requested to create a seller with email %s", info[0], info[4]);
        requestManager.addRequest(new Request(info[0],RequestType.REGISTER_SELLER,requestStr,seller));
    }

    public Manager createManager(String[] info){
        Manager manager =  new Manager(info[0],
                info[1],
                info[2],
                info[3],
                info[4],
                info[5],
                new Cart()
        );

        String managerJson = new Gson().toJson(manager);
        /*try {
            FileWriter fileWriter = new FileWriter("src/main/resources/users.user",true);
            fileWriter.write(managerJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return manager;
    }

    private Customer createCustomer(String[] info){
        Customer customer = new Customer(
                info[0],
                info[1],
                info[2],
                info[3],
                info[4],
                info[5],
                new Cart(),
                Long.parseLong(info[6])
        );

        String customerJson = new Gson().toJson(customer);
        /*try {
            FileWriter fileWriter = new FileWriter("src/main/resources/users.user",true);
            fileWriter.write(customerJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return customer;
    }

    public void login(String username,String password) throws WrongPasswordException {
        if (isCorrectPassword(username, password)){
            getUserByUsername(username).setHasSignedIn(true);
        } else {
            throw new WrongPasswordException(username);
        }
    }

    public void changeInfo(String[] info) throws SameInfoException {
        String username = info[0];
        String type = info[1];
        String previousInfo = info[2];
        String newInfo = info[3];

        if (previousInfo.equals(newInfo)){
            throw new  SameInfoException(type);
        }

        User user = getUserByUsername(username);

        switch (type){
            case "password" : user.setPassword(newInfo); break;
            case "firstName" : user.setFirstName(newInfo); break;
            case "lastName" : user.setLastName(newInfo); break;
            case "email" : user.setEmail(newInfo); break;
            case "phoneNumber" : user.setPhoneNumber(newInfo); break;
        }
    }

    public void logout(String username) {
        getUserByUsername(username).setHasSignedIn(false);
    }

    private boolean isCorrectPassword(String username,String password) {
        return getUserByUsername(username).getPassword().equals(password);
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        throw new UserNotAvailableException();
    }

    public boolean isUsernameAvailable(String username){
        for (User user : users) {
            if (user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public List<User> getUsers() {
        return users;
    }
}
