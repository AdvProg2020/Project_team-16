package controler;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.System.FilterManager;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.CategoryEditAttribute;
import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.category.RepeatedFeatureException;
import ModelPackage.System.exeption.category.RepeatedNameInParentCategoryException;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.System.exeption.product.EditorIsNotSellerException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import ModelPackage.Users.Customer;
import ModelPackage.Users.Request;
import ModelPackage.Users.User;
import View.FilterPackage;
import View.PrintModels.*;
import View.SortPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ManagerController extends Controller {
    private static ManagerController managerController = new ManagerController();

    public static ManagerController getInstance() {
        return managerController;
    }

    public List<UserFullPM> manageUsers () {
        List<User> users = DBManager.loadAllData(User.class);
        sortManager.sortUser(users);
        ArrayList<UserFullPM> userMiniPMS = new ArrayList<>();

        for (User user : users) {
            userMiniPMS.add(createUserFullPM(user));
        }

        return userMiniPMS;
    }

    public UserFullPM createUserFullPM(User user) {
        return new UserFullPM(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getClass().getName()
        );
    }

    public void deleteUser(String username) throws UserNotAvailableException {
        managerManager.deleteUser(username);
    }

    public void createManagerProfile(String[] info) {
        managerManager.createManagerProfile(info);
    }

    public List<MiniProductPM> manageProducts(SortPackage sortPackage, FilterPackage filterPackage) {
        List<Product> sortedProducts = sortManager.sort(DBManager.loadAllData(Product.class), sortPackage.getSortType());
        if (!sortPackage.isAscending()) Collections.reverse(sortedProducts);

        int[] priceRange = new int[2];
        priceRange[0] = filterPackage.getDownPriceLimit();
        priceRange[1] = filterPackage.getUpPriceLimit();
        List<Product> filteredProducts = FilterManager.filterList(sortedProducts, filterPackage.getActiveFilters(), priceRange);

        ArrayList<MiniProductPM> miniProductPMS = new ArrayList<>();

        for (Product product : filteredProducts) {
            miniProductPMS.add(createMiniProductPM(product));
        }

        return miniProductPMS;
    }

    private MiniProductPM createMiniProductPM(Product product){
        List<SellPackagePM> sellPackagePMs = new ArrayList<>();
        product.getPackages().forEach(sellPackage -> {
            int offPercent = sellPackage.isOnOff()? sellPackage.getOff().getOffPercentage() : 0;
            sellPackagePMs.add(new SellPackagePM(offPercent,
                    sellPackage.getPrice(),
                    sellPackage.getStock(),
                    sellPackage.getSeller().getUsername(),
                    sellPackage.isAvailable()));
        });
        return new MiniProductPM(product.getName(),
                product.getId(),
                product.getCategory().getName(),
                product.getCompanyClass().getName(),
                product.getTotalScore(),
                product.getDescription(),
                sellPackagePMs);
    }

    public void removeProduct(int productId) throws NoSuchAProductException {
        productManager.deleteProduct(productId);
    }

    public void createDiscount(String[] data, Date startTime, Date endTime) throws
            NotValidPercentageException, StartingDateIsAfterEndingDate, AlreadyExistCodeException {
        int offPercentage = Integer.parseInt(data[1]);
        long maxDiscount = Long.parseLong(data[2]);
        discountManager.createDiscountCode(data[0], startTime, endTime, offPercentage, maxDiscount);
    }

    public ArrayList<DisCodeManagerPM> viewDiscountCodes() {
        List<DiscountCode> discountCodes = DBManager.loadAllData(DiscountCode.class);
        ArrayList<DisCodeManagerPM> disCodeManagerPMS = new ArrayList<>();
        discountCodes.forEach(discountCode -> disCodeManagerPMS.add(createDiscountCodeManagerPM(discountCode)));
        return disCodeManagerPMS;
    }

    private DisCodeManagerPM createDiscountCodeManagerPM(DiscountCode discountCode){
        ArrayList<UserIntegerPM> list = new ArrayList<>();
        discountCode.getUsers().forEach(map -> list.add(new UserIntegerPM(map.getUser().getUsername(), map.getInteger())));
        return new DisCodeManagerPM(
                discountCode.getCode(),
                discountCode.getStartTime(),
                discountCode.getEndTime(),
                discountCode.getOffPercentage(),
                discountCode.getMaxDiscount(), list
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
        Customer user = DBManager.load(Customer.class, username);
        if (user == null) {
            throw new UserNotAvailableException();
        }
        discountManager.addUserToDiscountCodeUsers(code,user,time);
    }

    public void removeDiscountCode(String code) throws NoSuchADiscountCodeException {
        discountManager.removeDiscount(code);
    }

    public ArrayList<RequestPM> manageRequests() {
        ArrayList<Request> requests = requestManager.getRequestsForManager();
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

    public void acceptRequest(int id) throws NoSuchARequestException, NoSuchAProductException {
        requestManager.accept(id);
    }

    public void declineRequest(int id) throws NoSuchARequestException {
        requestManager.decline(id);
    }

    public ArrayList<CategoryPM> getAllCategories(){
        List<Category> cats = categoryManager.getBaseCats();
        sortManager.sortCategories(cats);
        ArrayList<CategoryPM> toReturn = new ArrayList<>();
        for (Category cat : cats) {
            toReturn.addAll(getAllCategoriesIn(0,cat));
        }
        return toReturn;
    }

    private List<CategoryPM> getAllCategoriesIn(int currentIndent,Category category){
        List<CategoryPM> toReturn = new ArrayList<>();
        toReturn.add(createCatPM(category,currentIndent));
        if (!category.getSubCategories().isEmpty())
            for (Category subCategory : category.getSubCategories()) {
                toReturn.addAll(getAllCategoriesIn(currentIndent+1,subCategory));
            }
        return toReturn;
    }

    private CategoryPM createCatPM(Category category,int indent){
        return new CategoryPM(category.getName(),category.getId(),indent);
    }

    public void editCategory(int id, CategoryEditAttribute editAttribute)
            throws RepeatedNameInParentCategoryException, NoSuchACategoryException, RepeatedFeatureException {
        categoryManager.editCategory(id, editAttribute);
    }

    public void addCategory(String name, int parentId, List<String> features) throws RepeatedNameInParentCategoryException, NoSuchACategoryException {
        categoryManager.createCategory(name, parentId,features);
    }

    public void removeCategory(int id) throws NoSuchACategoryException {
        categoryManager.removeCategory(id);
    }


}
