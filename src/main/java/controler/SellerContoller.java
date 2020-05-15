package controler;

import ModelPackage.Log.SellLog;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Off.Off;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.System.exeption.off.NoSuchAOffException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import View.PrintModels.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SellerContoller extends Controller{

    public CompanyPM viewCompanyInfo(String sellerUserName) {
         Company company = sellerManager.viewCompanyInformation(sellerUserName);
         return new CompanyPM(company.getName(), company.getGroup(), company.getPhone());
     }

    public List<SellLogPM> viewSalesHistory(String sellerUserName) {
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

    public long viewBalance(String sellerUserName) {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        return seller.getBalance();
    }

    public List<MiniProductPM> manageProducts(String sellerUserName) {
         Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
         List<Product> sellerProducts = seller.getProducts();
         ArrayList<MiniProductPM> miniProductPMs = new ArrayList<>();
        for (Product sellerProduct : sellerProducts) {
            miniProductPMs.add(createMiniProductPM(sellerProduct));
        }
        return miniProductPMs;
    }

    public FullProductPM viewProduct(int productId) throws NoSuchAProductException {
         Product product = productManager.findProductById(productId);
        return new FullProductPM(createMiniProductPM(product),
                productManager.allFeaturesOf(product));
    }

    public void removeProduct(int productId) throws NoSuchACategoryException,
            NoSuchAProductInCategoryException, NoSuchAProductException {
         productManager.deleteProduct(productId);
    }

    public List<OffPM> viewAllOffs(String sellerUserName) {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        List<Off> offs = seller.getOffs();
        List<OffPM> offPMs = new ArrayList<>();
        for (Off off : offs) {
            offPMs.add(new OffPM(off.getOffId(),
                    addProductIdsToOffPM(off),
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

    public void addOff(String[] data, String sellerUserName) throws ParseException, InvalidTimes {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        Date startTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[0]);
        Date endTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[1]);
        Date[] dates = new Date[2];
        dates[0] = startTime;
        dates[1] = endTime;
        int offPercentage = Integer.parseInt(data[2]);
        offManager.createOff(seller, dates, offPercentage);
    }

    /*public void editOff(String[] data) throws ParseException,
            NoSuchAOffException, NoSuchAProductException {
        String sellerUserName = data[0];
        User user = accountManager.getUserByUsername(sellerUserName);
        int editedOffId = Integer.parseInt(data[1]);
        String toChangeInfoType = data[2];
        switch (toChangeInfoType) {
            case "start time of off" : offManager.editStartTimeOfOff(user, editedOffId,
                        new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[3]));
            break;
            case "end time of off" : offManager.editEndTimeOfOff(user, editedOffId,
                        new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[3]));
            break;
            case "percentage of off" : offManager.editPercentageOfOff(user, editedOffId,
                    Integer.parseInt(data[3]));
            break;
            case "add product" : offManager.addProductToOff(user, editedOffId,
                    Integer.parseInt(data[3]));
            break;
            case "delete product" : offManager.deleteProductFromOff(user, editedOffId,
                    Integer.parseInt(data[3]));
        }
    }*/

    public UserFullPM viewSellerPersonalInfo(String sellerUserName) {
        User user = accountManager.viewPersonalInfo(sellerUserName);
        return new UserFullPM(user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getPhoneNumber(), "seller");
    }

    /*public void editSellerInfo(String[] data) {
        String[] info = new String[3];
        info[0] = data[0];
        info[1] = data[1];
        info[2] = data[2];
        accountManager.changeInfo(info);
    }*/

    public void addProduct(String[] data, String[] productPublicFeatures, String[] productSpecialFeatures) {
        String sellerUserName = data[0];
        String productName = data[1];
        String companyName = data[2];
        String categoryId = data[3];
        String description = data[4];
        int amountOfProduct = Integer.parseInt(data[5]);
        int priceOfProduct = Integer.parseInt(data[6]);
        ArrayList<Seller> sellers = addSellerToNewProduct(sellerUserName);
        List<SellerIntegerMap> stock = addStockToNewProduct(sellers.get(0), amountOfProduct);
        List<SellerIntegerMap> prices = addPriceToNewProduct(sellers.get(0), priceOfProduct);
        Product product = new Product(productName, companyName, sellers, categoryId,
                publicFeaturesOf(productPublicFeatures),
                specialFeaturesOf(productSpecialFeatures),
                description, stock, prices);
        productManager.createProduct(product, sellerUserName);
    }

    /*public void editProduct(String[] data) throws NoSuchAProductException {
        String sellerUserName = data[0];
        int productId = Integer.parseInt(data[1]);
        Product product = productManager.findProductById(productId);
        productManager.editProduct(product, sellerUserName);
    }*/

    private ArrayList<Seller> addSellerToNewProduct(String sellerUserName) {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
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
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription());
    }

}
