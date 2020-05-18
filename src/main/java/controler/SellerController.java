package controler;

import ModelPackage.Log.SellLog;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Off.Off;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.CategoryManager;
import ModelPackage.System.SortType;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.ProductEditAttribute;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.System.exeption.off.NoSuchAOffException;
import ModelPackage.System.exeption.product.EditorIsNotSellerException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import View.PrintModels.*;
import View.SortPackage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SellerController extends Controller{
    private static SellerController sellerController = new SellerController();

    public static SellerController getInstance() {
        return sellerController;
    }

    public CompanyPM viewCompanyInfo(String sellerUserName) throws UserNotAvailableException {
         Company company = sellerManager.viewCompanyInformation(sellerUserName);
         return new CompanyPM(company.getName(), company.getGroup(), company.getPhone());
     }

    public List<SellLogPM> viewSalesHistory(String sellerUserName) throws UserNotAvailableException {
        List<SellLog> sellLogs = sellerManager.viewSalesHistory(sellerUserName);
        ArrayList<SellLogPM> sellLogPMs = new ArrayList<>();
        for (SellLog sellLog : sellLogs) {
            sellLogPMs.add(new SellLogPM(sellLog.getProduct().getId(),
                    sellLog.getMoneyGotten(),
                    sellLog.getDiscount(),
                    sellLog.getBuyer().getUsername(),
                    sellLog.getDeliveryStatus()));
        }
        return sellLogPMs;
    }

    public long viewBalance(String sellerUserName) throws UserNotAvailableException {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        return seller.getBalance();
    }

    public List<MiniProductPM> manageProducts(String sellerUserName, SortPackage sort) throws UserNotAvailableException {
         List<Product> sellerProducts = sellerManager.viewProducts(sellerUserName);
         sortManager.sort(sellerProducts,sort.getSortType());
         if (!sort.isAscending()) Collections.reverse(sellerProducts);
         ArrayList<MiniProductPM> miniProductPMs = new ArrayList<>();
         for (Product sellerProduct : sellerProducts) {
             miniProductPMs.add(createMiniProductPM(sellerProduct));
        }
         return miniProductPMs;
    }

    public List<UserMiniPM> viewAllBuyersOfProduct(int productId) throws NoSuchAProductException {
        List<UserMiniPM> userMiniPMs = new ArrayList<>();
        List<User> allBuyers = new ArrayList<>();
        Product product = productManager.findProductById(productId);
        for (Seller seller : product.getAllSellers()) {
            allBuyers.addAll(csclManager.allBuyers(productId, seller));
        }
        allBuyers = sortManager.sortUser(allBuyers);
        for (User buyer : allBuyers) {
            userMiniPMs.add(createUserMiniPM(buyer));
        }
        return userMiniPMs;
    }

    public FullProductPM viewProduct(int productId) throws NoSuchAProductException {
         Product product = productManager.findProductById(productId);
        return new FullProductPM(createMiniProductPM(product),
                productManager.allFeaturesOf(product));
    }

    public void removeProduct(int productId,String editor) throws NoSuchACategoryException,
            NoSuchAProductInCategoryException, NoSuchAProductException, EditorIsNotSellerException {
         productManager.deleteProduct(productId,editor);
    }

    public List<MiniOffPM> viewAllOffs(String sellerUserName) throws UserNotAvailableException {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        List<Off> offs = seller.getOffs();
        List<MiniOffPM> offPMs = new ArrayList<>();
        for (Off off : offs) {
            offPMs.add(new MiniOffPM(off.getOffId(),
                    off.getSeller().getUsername(),
                    off.getStartTime(),
                    off.getEndTime(),
                    off.getOffPercentage()));
        }
        return offPMs;
    }

    public OffPM viewOff(int offId) throws NoSuchAOffException {
        Off off = offManager.findOffById(offId);
        return new OffPM(off.getOffId(),
                addProductIdsToOffPM(off),
                off.getSeller().getUsername(),
                off.getStartTime(),
                off.getEndTime(),
                off.getOffPercentage());
    }

    public void addOff(String[] data, String sellerUserName) throws ParseException, InvalidTimes, UserNotAvailableException {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        Date startTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[0]);
        Date endTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[1]);
        Date[] dates = new Date[2];
        dates[0] = startTime;
        dates[1] = endTime;
        int offPercentage = Integer.parseInt(data[2]);
        offManager.createOff(seller, dates, offPercentage);
    }

    public void editOff(String[] data, OffChangeAttributes editAttributes)
            throws InvalidTimes, NoSuchAOffException, NoSuchAProductException,
            UserNotAvailableException {
        int offId = Integer.parseInt(data[0]);
        String userName = data[1];
        Seller seller = DBManager.load(Seller.class,userName);
        if (seller == null) {
            throw new UserNotAvailableException();
        }
        Date start = editAttributes.getStart();
        Date end = editAttributes.getEnd();
        int productIdToRemove = editAttributes.getProductIdToRemove();
        int productIdToAdd = editAttributes.getProductIdToAdd();
        int percentage = editAttributes.getPercentage();
        if (start != null)
            offManager.editStartTimeOfOff(seller, offId, start);
        if (end != null)
            offManager.editEndTimeOfOff(seller, offId, end);
        if (productIdToRemove != 0)
            offManager.deleteProductFromOff(seller, offId, productIdToRemove);
        if (productIdToAdd != 0)
            offManager.addProductToOff(seller, offId, productIdToAdd);
        if (percentage != 0)
            offManager.editPercentageOfOff(seller, offId, percentage);
    }

    public void deleteOff(String data) throws NoSuchAOffException {
        int offId = Integer.parseInt(data);
        offManager.deleteOff(offId);
    }

    public void addProduct(String[] data, String[] productPublicFeatures, String[] productSpecialFeatures)
            throws NoSuchACategoryException, UserNotAvailableException {
        String sellerUserName = data[0];
        String productName = data[1];
        String companyName = data[2];
        String categoryId = data[3];
        Category category = categoryManager.getCategoryById(Integer.parseInt(categoryId));
        String description = data[4];
        int amountOfProduct = Integer.parseInt(data[5]);
        int priceOfProduct = Integer.parseInt(data[6]);
        ArrayList<Seller> sellers = addSellerToNewProduct(sellerUserName);
        List<SellerIntegerMap> stock = addStockToNewProduct(sellers.get(0), amountOfProduct);
        List<SellerIntegerMap> prices = addPriceToNewProduct(sellers.get(0), priceOfProduct);
        Product product = new Product(productName, companyName, sellers, category,
                publicFeaturesOf(productPublicFeatures),
                specialFeaturesOf(productSpecialFeatures),
                description, stock, prices);
        productManager.createProduct(product, sellerUserName);
    }

    public List<String> getSpecialFeaturesOfCat(int catId) throws NoSuchACategoryException {
        return categoryManager.getAllSpecialFeaturesFromCategory(catId);
    }

    public List<String> getPublicFeatures(){
        return CategoryManager.getPublicFeatures();
    }

    public void editProduct(String sellerUserName, ProductEditAttribute editAttribute)
            throws NoSuchAProductException, EditorIsNotSellerException {
        productManager.editProduct(editAttribute, sellerUserName);
    }

    private ArrayList<Seller> addSellerToNewProduct(String sellerUserName) throws UserNotAvailableException {
        Seller seller = DBManager.load(Seller.class,sellerUserName);
        if (seller == null) {
            throw new UserNotAvailableException();
        }
        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        return sellers;
    }

    private List<SellerIntegerMap> addStockToNewProduct(Seller seller, int amountOfProduct) {
        List<SellerIntegerMap> stock = new ArrayList<>();
        stock.add(new SellerIntegerMap(seller, amountOfProduct));
        return stock;
    }

    private List<SellerIntegerMap> addPriceToNewProduct(Seller seller, int newPrice) {
        List<SellerIntegerMap> prices = new ArrayList<>();
        prices.add(new SellerIntegerMap(seller, newPrice));
        return prices;
    }

    private HashMap<String,String> publicFeaturesOf(String[] publicFeatureData) {
        HashMap<String,String> publicFeatures = new HashMap<>();
        for (int i = 0; i < publicFeatureData.length; i += 2) {
            publicFeatures.put(publicFeatureData[i], publicFeatureData[i + 1]);
        }
        return publicFeatures;
    }

    private HashMap<String,String> specialFeaturesOf(String[] specialFeatureData) {
        HashMap<String,String> specialFeatures = new HashMap<>();
        for (int i = 0; i < specialFeatureData.length; i += 2) {
            specialFeatures.put(specialFeatureData[i], specialFeatureData[i + 1]);
        }
        return specialFeatures;
    }

    private ArrayList<Integer> addProductIdsToOffPM(Off off) {
        ArrayList<Integer> productIds = new ArrayList<>();
        for (Product product : off.getProducts()) {
            productIds.add(product.getId());
        }
        return productIds;
    }

    private MiniProductPM createMiniProductPM(Product product) {
        return new MiniProductPM(product.getName(),
                product.getId(),
                product.getCategory().getName(),
                product.getStock(),
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription());
    }

    private UserMiniPM createUserMiniPM(User user) {
        return new UserMiniPM(user.getUsername(), "buyer");
    }



}
