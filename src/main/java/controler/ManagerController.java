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
import View.PrintModels.MiniProductPM;
import View.PrintModels.UserFullPM;
import View.PrintModels.UserMiniPM;

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

    public List<DiscountCode> viewDiscountCodes(){
        return DBManager.loadAllData(DiscountCode.class);
    }

    public DiscountCode viewDiscountCode(String code) throws NoSuchADiscountCodeException {
        return discountManager.showDiscountCode(code);
    }

    public void editDiscountCode(){

    }
}
