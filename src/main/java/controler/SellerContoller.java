package controler;

import ModelPackage.Log.SellLog;
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
                mergeProductFeatures(product));
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

    public UserFullPM viewSellerPersonalInfo(String sellerUserName) {
        User user = accountManager.getUserByUsername(sellerUserName);
        return new UserFullPM(user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getPhoneNumber(), "seller");
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

    private Map<String, String> mergeProductFeatures(Product product) {
        Map <String, String> features = new HashMap<>();
        features.putAll(product.getPublicFeatures());
        features.putAll(product.getSpecialFeatures());
        return features;
    }

}
