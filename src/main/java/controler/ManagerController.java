package controler;

import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.discount.NotValidPercentageException;
import ModelPackage.System.exeption.discount.StartingDateIsAfterEndingDate;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ManagerController extends Controller {

    public List<User> manageUsers () {
        return DBManager.loadAllData(User.class);
    }

    public User viewUser(String username){
        return accountManager.getUserByUsername(username);
    }

    public void deleteUser(String username){
        managerManager.deleteUser(username);
    }

    public void createManagerProfile(String[] info){
        managerManager.createManagerProfile(info);
    }

    public List<Product> manageProducts(){
        return productManager.getAllProducts();
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
}
