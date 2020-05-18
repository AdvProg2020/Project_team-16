package controler;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.CategoryEditAttribute;
import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.System.exeption.account.SecondManagerByUserException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.category.RepeatedFeatureException;
import ModelPackage.System.exeption.category.RepeatedNameInParentCategoryExeption;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.System.exeption.product.AlreadyASeller;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import ModelPackage.Users.Request;
import ModelPackage.Users.User;
import View.PrintModels.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManagerController extends Controller {
    private static ManagerController managerController = new ManagerController();

    public static ManagerController getInstance() {
        return managerController;
    }

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
                user.getClass().getName()
        );
    }

    public UserFullPM viewUser(String username) throws UserNotAvailableException {
        User user = accountManager.getUserByUsername(username);
        return new UserFullPM(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getClass().getName()
        );
    }

    public void deleteUser(String username){
        managerManager.deleteUser(username);
    }

    public void createManagerProfile(String[] info) throws SecondManagerByUserException {
        managerManager.createManagerProfile(info);
    }

    public List<MiniProductPM> manageProducts(){
        List<Product> products = DBManager.loadAllData(Product.class);
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
                product.getCategory().getName(),
                product.getStock(),
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
            NotValidPercentageException, StartingDateIsAfterEndingDate, AlreadyExistCodeException {
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data[0]);
        Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data[1]);
        int offPercentage = Integer.parseInt(data[2]);
        long maxDiscount = Long.parseLong(data[3]);
        discountManager.createDiscountCode(data[4],startTime, endTime, offPercentage, maxDiscount);
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

    public void editDiscountCode(String code, DiscountCodeEditAttributes editAttributes)
            throws NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate, NoSuchADiscountCodeException {
        discountManager.editDiscountCode(code, editAttributes);
    }

    public void removeUserFromDiscountCodeUsers(String code, String username) throws UserNotExistedInDiscountCodeException, NoSuchADiscountCodeException, UserNotAvailableException {
        User user = accountManager.getUserByUsername(username);
        discountManager.removeUserFromDiscountCodeUsers(code, user);
    }

    public void addUserToDiscountCode(String code,String username,int time) throws UserNotAvailableException, UserExistedInDiscountCodeException, NoSuchADiscountCodeException {
        User user = accountManager.getUserByUsername(username);
        discountManager.addUserToDiscountCodeUsers(code,user,time);
    }

    public void removeDiscountCode(String code) throws NoSuchADiscountCodeException {
        discountManager.removeDiscount(code);
    }

    public List<RequestPM> manageRequests(){
        List<Request> requests = DBManager.loadAllData(Request.class);
        ArrayList<RequestPM> requestPMS = new ArrayList<>();

        for (Request request : requests) {
            requestPMS.add(createRequestPM(request));
        }

        return requestPMS;
    }

    private RequestPM createRequestPM(Request request){
        return new RequestPM(
                request.getUserHasRequested(),
                request.getRequestId(),
                request.getRequestType().toString(),
                request.getRequest()
        );
    }

    public RequestPM viewRequest(int id) throws NoSuchARequestException {
        Request request = requestManager.findRequestById(id);
        return new RequestPM(
                request.getUserHasRequested(),
                request.getRequestId(),
                request.getRequestType().toString(),
                request.getRequest()
        );
    }

    public void acceptRequest(int id) throws NoSuchARequestException, AlreadyASeller, NoSuchAProductException {
        requestManager.accept(id);
    }

    public void declineRequest(int id) throws NoSuchARequestException {
        requestManager.decline(id);
    }

    public List<CategoryPM> manageCategories(){
        List<Category> categories = DBManager.loadAllData(Category.class);
        ArrayList<CategoryPM> categoryPMS = new ArrayList<>();

        for (Category category : categories) {
            categoryPMS.add(createCategoryPM(category));
        }

        return categoryPMS;
    }

    private CategoryPM createCategoryPM(Category category){
        return new CategoryPM(
                category.getName(),
                category.getId()
        );
    }

    public void editCategory(int id, CategoryEditAttribute editAttribute)
            throws RepeatedNameInParentCategoryExeption, NoSuchACategoryException, RepeatedFeatureException {
        categoryManager.editCategory(id, editAttribute);
    }

    public void addCategory(String name, int parentId,List<String> features) throws RepeatedNameInParentCategoryExeption, NoSuchACategoryException {
        categoryManager.createCategory(name, parentId);
    }

    public void removeCategory(int id) throws NoSuchACategoryException {
        categoryManager.removeCategory(id);
    }


}
