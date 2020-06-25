package controler;

import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.SecondManagerByUserException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.account.WrongPasswordException;
import ModelPackage.Users.*;
import View.Main;
import View.PrintModels.RequestPM;
import View.PrintModels.UserFullPM;
import controler.exceptions.ManagerExist;
import javafx.scene.image.Image;

import java.io.*;
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

    public boolean isTheFirstManager() {
        try {
            managerManager.checkIfIsTheFirstManager();
            return true;
        } catch (SecondManagerByUserException ignore) {
            return false;
        }
    }

    public String login(String username, String password) throws NotVerifiedSeller, UserNotAvailableException, WrongPasswordException {
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

    public Image userImage(String username) {
        File file = new File("src\\main\\resources\\db\\images\\users\\" + username + ".jpg");
        if (file.exists()) {
            return new Image(String.valueOf(file.toURI()));
        }
        return null;
    }

    public void saveNewImage(InputStream inputStream, String username) throws IOException {
        File file = new File("src\\main\\resources\\db\\images\\users\\" + username + ".jpg");
        byte[] buffer = new byte[inputStream.available()];
        if (!file.exists()) file.createNewFile();
        inputStream.read(buffer);
        OutputStream outStream = new FileOutputStream(file);
        outStream.write(buffer);
        outStream.close();
    }

    public List<RequestPM> viewRequestSent(String username, String role) {
        List<Request> requests = new ArrayList<>();
        if (role.equalsIgnoreCase("seller")) {
            Seller seller = DBManager.load(Seller.class, username);
            if (seller != null) {
                requests = seller.getRequests();
            }
        } else if (role.equalsIgnoreCase("customer")) {
            Customer customer = DBManager.load(Customer.class, username);
            if (customer != null) {
                requests = customer.getRequests();
            }
        }

        if (requests == null | requests.isEmpty()) {
            return null;
        } else {
            return makeRequestPMs(requests);
        }
    }

    private List<RequestPM> makeRequestPMs(List<Request> requests) {
        List<RequestPM> requestPMS = new ArrayList<>();
        requests.forEach(req -> requestPMS.add(newReqPm(req)));
        return requestPMS;
    }

    private RequestPM newReqPm(Request req) {
        String status;
        if (!req.isDone()) {
            status = "Not Considered Yet";
        } else {
            if (req.isAccepted()) {
                status = "Accepted";
            } else {
                status = "Rejected";
            }
        }
        return new RequestPM(req.getRequestId(), status, req.getRequest());
    }
}
