package controler;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.discount.NotValidPercentageException;
import ModelPackage.System.exeption.discount.StartingDateIsAfterEndingDate;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.User;
import View.PrintModels.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManagerController extends Controller {

    public List<UserMiniPM> manageUsers () {
        List<User> users = DBManager.loadAllData(User.class);
        ArrayList<UserMiniPM> userMiniPMS = new ArrayList<>();

        for (User user : users) {
            userMiniPMS.add(createUserMiniPM(user));
        }

        return userMiniPMS;
    }

    private UserMiniPM createUserMiniPM(User user){
        return new UserMiniPM(
                user.getUsername(),
                user.getClass().toString().split(" ")[1]
        );
    }

    public UserFullPM viewUser(String username){
        User user = accountManager.getUserByUsername(username);
        return new UserFullPM(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getClass().toString().split(" ")[1]
        );
    }

    public void deleteUser(String username){
        managerManager.deleteUser(username);
    }

    public void createManagerProfile(String[] info){
        managerManager.createManagerProfile(info);
    }

    public List<MiniProductPM> manageProducts(){
        ArrayList<Product> products = productManager.getAllProducts();
        ArrayList<MiniProductPM> miniProductPMS = new ArrayList<>();

        for (Product product : products) {
            miniProductPMS.add(createMiniProductPM(product));
        }

        return miniProductPMS;
    }

    private MiniProductPM createMiniProductPM(Product product){
        return new MiniProductPM(
                product.getName(),
                product.getId(),
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription()
        );
    }

    public void removeProduct(int productId) throws NoSuchACategoryException,
            NoSuchAProductInCategoryException, NoSuchAProductException {
        productManager.deleteProduct(productId);
    }

    public void createDiscount(String[] data) throws ParseException,
            NotValidPercentageException, StartingDateIsAfterEndingDate {
        Date startTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[0]);
        Date endTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[1]);
        int offPercentage = Integer.parseInt(data[2]);
        long maxDiscount = Long.parseLong(data[3]);
        discountManager.createDiscountCode(startTime, endTime, offPercentage, maxDiscount);
    }

    public List<DiscountMiniPM> viewDiscountCodes(){
        List<DiscountCode> discountCodes = DBManager.loadAllData(DiscountCode.class);
        ArrayList<DiscountMiniPM> disCodeManagerPMS = new ArrayList<>();

        for (DiscountCode discountCode : discountCodes) {
            disCodeManagerPMS.add(createDiscountCodePM(discountCode));
        }

        return disCodeManagerPMS;
    }

    private DiscountMiniPM createDiscountCodePM(DiscountCode discountCode){
        return new DiscountMiniPM(
                discountCode.getCode(),
                discountCode.getOffPercentage()
        );
    }

    public DisCodeManagerPM viewDiscountCode(String code) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = discountManager.showDiscountCode(code);
        return createDiscountCodeManagerPM(discountCode);
    }

    private DisCodeManagerPM createDiscountCodeManagerPM(DiscountCode discountCode){
        return new DisCodeManagerPM(
                discountCode.getCode(),
                discountCode.getStartTime(),
                discountCode.getEndTime(),
                discountCode.getOffPercentage(),
                discountCode.getMaxDiscount(),
                discountCode.getUsers()
        );
    }

    public void editDiscountCode(String code, String whatToEdit, String newInfo) throws NoSuchADiscountCodeException,
            ParseException, StartingDateIsAfterEndingDate {

        switch (whatToEdit){
            case "starting date" : editDiscountStartingDate(code, newInfo); break;
            case "ending date" : editDiscountEndingDate(code, newInfo); break;

        }
    }

    private void editDiscountStartingDate(String code, String newDate) throws ParseException,
            StartingDateIsAfterEndingDate, NoSuchADiscountCodeException {
        Date startTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(newDate);

        discountManager.editDiscountStartingDate(code, startTime);
    }

    private void editDiscountEndingDate(String code, String newDate) throws ParseException,
            StartingDateIsAfterEndingDate, NoSuchADiscountCodeException {
        Date endTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(newDate);

        discountManager.editDiscountStartingDate(code, endTime);
    }
}
