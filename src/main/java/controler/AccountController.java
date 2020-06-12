package controler;

import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.SecondManagerByUserException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.SubCart;
import ModelPackage.Users.User;
import View.PrintModels.UserFullPM;
import controler.exceptions.ManagerExist;

import java.util.ArrayList;
import java.util.List;

public class AccountController extends Controller {
    private static AccountController accountController = new AccountController();

    public static AccountController getInstance() {
        return accountController;
    }

    public void usernameInitialCheck(String username) throws UserNotAvailableException {
        if (accountManager.isUsernameAvailable(username)){
            throw new UserNotAvailableException();
        }
    }

    public void createAccount(String[] info, String type){
        accountManager.createAccount(info, type);
    }

    public void createFirstManager(String[] info) throws ManagerExist {
        try {
            managerManager.checkIfIsTheFirstManager();
            managerManager.createManagerProfile(info);
        } catch (SecondManagerByUserException e){
            throw new ManagerExist();
        }
    }

    public String login(String username, String password) throws NotVerifiedSeller, UserNotAvailableException {
        return accountManager.login(username, password);
    }

    public UserFullPM viewPersonalInfo(String username) throws UserNotAvailableException {
        User user = accountManager.viewPersonalInfo(username);
        return new UserFullPM(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getClass().getName().split("\\.")[2]
        );
    }

    public void editPersonalInfo(String username, UserEditAttributes editAttributes) throws UserNotAvailableException {
        accountManager.changeInfo(username, editAttributes);
    }

    public void logout(String username) throws UserNotAvailableException {
        accountManager.logout(username);
    }

    private void addCartToCustomer(String username, Cart cart) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart previousCart = customer.getCart();

        boolean added;
        for (SubCart subCart : cart.getSubCarts()) {
            added = false;
            Product product = subCart.getProduct();
            for (SubCart previousCartSubCart : previousCart.getSubCarts()) {
                Product previousCartProduct = previousCartSubCart.getProduct();
                if (previousCartProduct.getId() == product.getId() &&
                        previousCartSubCart.getSeller().getUsername().equals(subCart.getSeller().getUsername())){
                    previousCartSubCart.setAmount(previousCartSubCart.getAmount() + subCart.getAmount());
                    added = true;
                    break;
                }
            }
            if (!added){
                previousCart.getSubCarts().add(subCart);
            }
        }
    }

    public int createCompany(String[] info){
        Company company = new Company(info[0],info[1],info[2]);
        return csclManager.createCompany(company);
    }
}
