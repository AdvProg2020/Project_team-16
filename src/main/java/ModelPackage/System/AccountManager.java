package ModelPackage.System;

import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.account.*;
import ModelPackage.Users.*;


public class AccountManager {
    private static AccountManager accountManager = null;
    private AccountManager(){

    }
    public static AccountManager getInstance(){
        if (accountManager == null)
            accountManager = new AccountManager();
        return accountManager;
    }

    private RequestManager requestManager = RequestManager.getInstance();
    private CSCLManager csclManager = CSCLManager.getInstance();

    public void createAccount(String[] info, String type) {
        switch (type){
            case "seller" : createSeller(info); break;
            case "manager" : Manager manager = createManager(info);DBManager.save(manager); break;
            case  "customer" : Customer customer = createCustomer(info);DBManager.save(customer); break;
        }
    }

    private void createSeller(String[] info) {
        Seller seller = new Seller(
                info[0],
                info[1],
                info[2],
                info[3],
                info[4],
                info[5],
                new Cart(),
                csclManager.getCompanyById(Integer.parseInt(info[6])),
                Long.parseLong(info[7]));
        String requestStr = String.format("%s has requested to create a seller with email %s", info[0], info[4]);
        requestManager.addRequest(new Request(info[0],RequestType.REGISTER_SELLER,requestStr,seller));
    }

    public Manager createManager(String[] info){
        return new Manager(info[0],
                info[1],
                info[2],
                info[3],
                info[4],
                info[5],
                new Cart()
        );
    }

    private Customer createCustomer(String[] info){
        return new Customer(
                info[0],
                info[1],
                info[2],
                info[3],
                info[4],
                info[5],
                new Cart(),
                Long.parseLong(info[6])
        );
    }

    public String login(String username,String password) throws NotVerifiedSeller, UserNotAvailableException, WrongPasswordException {
        User user = getUserByUsername(username);

        if (isCorrectPassword(user, password)){
            Customer customer = DBManager.load(Customer.class,username);
            if (customer != null) return "Customer";
            Seller seller = DBManager.load(Seller.class,username);
            if (seller != null) if (seller.getVerified())
                return "Seller";
            else
                throw new NotVerifiedSeller();
            Manager manager = DBManager.load(Manager.class,username);
            if (manager != null) {
                return "Manager";
            }
        } else {
            throw new WrongPasswordException(username);
        }
        return "";
    }

    public User viewPersonalInfo(String username) throws UserNotAvailableException {
        User user = getUserByUsername(username);
        checkIfUserHasLoggedIn(user);
        return user;
    }

    public void changeInfo(String username,UserEditAttributes editAttributes) throws UserNotAvailableException {

        User user = getUserByUsername(username);
        String newFirstName = editAttributes.getNewFirstName();
        String newLastName = editAttributes.getNewLastName();
        String newPassword = editAttributes.getNewPassword();
        String newEmail = editAttributes.getNewEmail();
        String newPhone = editAttributes.getNewPhone();

        if (newFirstName != null){
            user.setFirstName(newFirstName);
        }
        if (newLastName != null){
            user.setLastName(newLastName);
        }
        if (newPassword != null){
            user.setPassword(newPassword);
        }
        if (newEmail != null){
            user.setEmail(newEmail);
        }
        if (newPhone != null){
            user.setPhoneNumber(newPhone);
        }

        DBManager.save(user);
    }


    public void logout(String username) throws UserNotAvailableException {
        User user = getUserByUsername(username);
        checkIfUserHasLoggedIn(user);
        user.setHasSignedIn(false);
        DBManager.save(user);
    }

    private boolean isCorrectPassword(User username,String password) {
        return username.getPassword().equals(password);
    }

    public User getUserByUsername(String username) throws UserNotAvailableException {
        User user = DBManager.load(User.class,username);
        if (user == null)
            throw new UserNotAvailableException();
        return user;
    }

    public boolean isUsernameAvailable(String username){
        User user = DBManager.load(User.class,username);
        return user != null;
    }

    private void checkIfUserHasLoggedIn(User user){
        if (!user.isHasSignedIn()){
            throw new UserNotLoggedInException();
        }
    }

}
