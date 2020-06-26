package controler;

import ModelPackage.Log.SellLog;
import ModelPackage.Off.Off;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.System.CategoryManager;
import ModelPackage.System.FilterManager;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.ProductEditAttribute;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.System.exeption.off.NoSuchAOffException;
import ModelPackage.System.exeption.off.ThisOffDoesNotBelongssToYouException;
import ModelPackage.System.exeption.product.EditorIsNotSellerException;
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Seller;
import View.FilterPackage;
import View.PrintModels.*;
import View.SortPackage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class SellerController extends Controller{
    private static SellerController sellerController = new SellerController();

    public static SellerController getInstance() {
        return sellerController;
    }

    public CompanyPM viewCompanyInfo(String sellerUserName) throws UserNotAvailableException {
         Company company = sellerManager.viewCompanyInformation(sellerUserName);
        return new CompanyPM(company.getId(), company.getName(), company.getPhone(), company.getGroup());
     }

    public List<SellLogPM> viewSalesHistory(String sellerUserName) throws UserNotAvailableException {
        List<SellLog> sellLogs = sellerManager.viewSalesHistory(sellerUserName);
        ArrayList<SellLogPM> sellLogPMs = new ArrayList<>();
        for (SellLog sellLog : sellLogs) {
            sellLogPMs.add(new SellLogPM(sellLog.getLogId(),
                    sellLog.getProduct().getSourceId(),
                    sellLog.getMoneyGotten(),
                    sellLog.getDiscount(),
                    sellLog.getDate(),
                    sellLog.getBuyer().getUsername(),
                    sellLog.getDeliveryStatus()));
        }
        return sellLogPMs;
    }

    public void becomeSellerOfExistingProgram(int[] info,String sellerUsername) throws NoSuchAProductException, UserNotAvailableException {
        Product product = productManager.findProductById(info[0]);
        Seller seller = DBManager.load(Seller.class,sellerUsername);
        if (seller != null) {
            productManager.addASellerToProduct(product,seller,info[1],info[2]);
        }else throw new UserNotAvailableException();
    }

    public long viewBalance(String sellerUserName) throws UserNotAvailableException {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        return seller.getBalance();
    }

    public List<MiniProductPM> manageProducts(String sellerUserName, SortPackage sort) throws UserNotAvailableException {
         List<Product> sellerProducts = sellerManager.viewProducts(sellerUserName);
        //List<Product> sortedSellerProducts = sortManager.sort(sellerProducts,sort.getSortType());
        //if (!sort.isAscending()) Collections.reverse(sortedSellerProducts);
         ArrayList<MiniProductPM> miniProductPMs = new ArrayList<>();
        for (Product sellerProduct : sellerProducts) {
             miniProductPMs.add(createMiniProductPM(sellerProduct));
        }
         return miniProductPMs;
    }

    public List<MiniProductPM> manageProducts(String sellerUserName, SortPackage sortPackage, FilterPackage filterPackage)
            throws UserNotAvailableException {
        List<Product> sortedSellerProducts = sortManager.sort(sellerManager.viewProducts(sellerUserName), sortPackage.getSortType());
        int[] priceRange = new int[2];
        priceRange[0] = filterPackage.getDownPriceLimit();
        priceRange[1] = filterPackage.getUpPriceLimit();
        List<Product> filteredSellerProducts = FilterManager.filterList(sortedSellerProducts, filterPackage.getActiveFilters(), priceRange);
        ArrayList<MiniProductPM> miniProductPMs = new ArrayList<>();
        //System.err.println(filteredSellerProducts);
        filteredSellerProducts.forEach(product -> miniProductPMs.add(createMiniProductPM(product)));
        return miniProductPMs;
    }

    public List<MicroProduct> getProductsForSeller(String seller) throws UserNotAvailableException {
        List<Product> sellerProducts = sellerManager.viewProducts(seller);
        List<MicroProduct> list = new ArrayList<>();
        sellerProducts.forEach(product -> list.add(new MicroProduct(product.getName(), product.getId())));
        return list;
    }

    // TODO: 6/11/2020 If Is needed later will be impl
    /*
    public List<UserMiniPM> viewAllBuyersOfProduct(int productId,String viwer,SortPackage sortPackage) throws NoSuchAProductException, YouAreNotASellerException {
        List<UserMiniPM> userMiniPMs = new ArrayList<>();
        Product product = productManager.findProductById(productId);
        csclManager.checkIfIsASellerOFProduct(product,viwer);
        Seller seller = DBManager.load(Seller.class,viwer);
        List<User> allBuyers = new ArrayList<>(csclManager.allBuyers(productId, seller));
        allBuyers = sortManager.sortUser(allBuyers);
        if (!sortPackage.isAscending())Collections.reverse(allBuyers);
        for (User buyer : allBuyers) {
            userMiniPMs.add(createUserMiniPM(buyer));
        }
        return userMiniPMs;
    }*/

    public void removeProduct(int productId, String editor) throws NoSuchAPackageException {
        sellerManager.deleteProductForSeller(editor, productId);
    }

    public List<OffPM> viewAllOffs(String sellerUserName,SortPackage sortPackage) throws UserNotAvailableException {
        Seller seller = DBManager.load(Seller.class,sellerUserName);
        if (seller == null) {
            throw new UserNotAvailableException();
        }
        List<Off> offs = seller.getOffs();
        sortManager.sortOff(offs);
        if (!sortPackage.isAscending()) Collections.reverse(offs);
        List<OffPM> offPMs = new ArrayList<>();
        for (Off off : offs) {
            offPMs.add(new OffPM(off.getOffId(),
                    addProductToOffPM(off),
                    off.getSeller().getUsername(),
                    off.getStartTime(),
                    off.getEndTime(),
                    off.getOffPercentage(),
                    off.getOffStatus().toString()));
        }
        return offPMs;
    }

    public void addOff(Date start, Date end, int percentage, String sellerUserName) throws ParseException, InvalidTimes, UserNotAvailableException {
        Seller seller = (Seller) accountManager.getUserByUsername(sellerUserName);
        Date[] dates = new Date[2];
        dates[0] = start;
        dates[1] = end;
        offManager.createOff(seller, dates, percentage);
    }

    public void editOff(String seller, OffChangeAttributes editAttributes) throws ThisOffDoesNotBelongssToYouException, NoSuchAOffException {
        offManager.editOff(editAttributes,seller);
    }

    public void deleteOff(int id, String remover) throws NoSuchAOffException, ThisOffDoesNotBelongssToYouException {
        offManager.deleteOff(id,remover);
    }

    public int addProduct(String[] data, HashMap<String, String> publicFeature, HashMap<String, String> specialFeature)
            throws NoSuchACategoryException, UserNotAvailableException {
        String sellerUserName = data[0];
        String productName = data[1];
        int companyName = Integer.parseInt(data[2]);
        String categoryId = data[3];
        Category category = categoryManager.getCategoryById(Integer.parseInt(categoryId));
        String description = data[4];
        Seller seller = DBManager.load(Seller.class,sellerUserName);
        if (seller == null) throw new UserNotAvailableException();
        int amountOfProduct = Integer.parseInt(data[5]);
        int priceOfProduct = Integer.parseInt(data[6]);
        SellPackage sellPackage = new SellPackage(null,seller,priceOfProduct,amountOfProduct,null,false,true);
        DBManager.save(sellPackage);
        Company company = DBManager.load(Company.class, companyName);
        if (company == null) {
            throw new RuntimeException("Invalid Company");
        }
        Product product = new Product(productName, company, category,
                publicFeature,
                specialFeature,
                description,sellPackage);
        product.setLeastPrice(priceOfProduct);
        sellPackage.setProduct(product);
        DBManager.save(sellPackage);
        return productManager.createProduct(product, sellerUserName);
    }

    public ArrayList<String> getSpecialFeaturesOfCat(int catId) throws NoSuchACategoryException {
        ArrayList<String> features = new ArrayList<>(CategoryManager.getPublicFeatures());
        features.addAll(categoryManager.getAllSpecialFeaturesFromCategory(catId));
        return features;
    }

    public ArrayList<String> getSpecialFeatureOfCategory(int id) throws NoSuchACategoryException {
        ArrayList<String> features = new ArrayList<>();
        features.addAll(categoryManager.getAllSpecialFeaturesFromCategory(id));
        return features;
    }

    public List<String> getPublicFeatures(){
        return CategoryManager.getPublicFeatures();
    }

    public void editProduct(String sellerUserName, ProductEditAttribute editAttribute)
            throws NoSuchAProductException, EditorIsNotSellerException {
        productManager.editProduct(editAttribute, sellerUserName);
    }

    private ArrayList<MiniProductPM> addProductToOffPM(Off off) {
        ArrayList<MiniProductPM> products = new ArrayList<>();
        for (Product product : off.getProducts()) {
            products.add(createMiniProductPM(product));
        }
        return products;
    }

    private MiniProductPM createMiniProductPM(Product product) {
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

    public void saveImagesForProduct(int id, InputStream mainImage, ArrayList<InputStream> files) {
        File directory = new File("src/main/resources/db/images/products/" + id);
        if (!directory.exists()) directory.mkdirs();
        saveMainImage(id, mainImage);
        files.forEach(file -> saveImageForProduct(id, file));
    }

    private void saveMainImage(int id, InputStream data) {
        File image = new File("src/main/resources/db/images/products/" + id + "/main.jpg");
        if (!image.exists()) {
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            saveDataToFile(data, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToFile(InputStream data, File file) throws IOException {
        byte[] buffer = new byte[data.available()];
        data.read(buffer);
        OutputStream outStream = new FileOutputStream(file);
        outStream.write(buffer);
        outStream.close();
    }

    private void saveImageForProduct(int id, InputStream data) {
        File directory = new File("src/main/resources/db/images/products/" + id + "/other");
        directory.mkdirs();
        File image = new File("src/main/resources/db/images/products/" + id + "/other/" + generateUniqueFileName() + ".jpg");
        try {
            if (image.createNewFile()) {
                saveDataToFile(data, image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProductPicture(int id, InputStream mainImage, ArrayList<InputStream> files) {
        File directory = new File("src/main/resources/db/images/products/" + id);
        try {
            FileUtils.cleanDirectory(directory);
            saveImagesForProduct(id, mainImage, files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addVideo(int id, InputStream data) {
        File video = new File("src/main/resources/db/videos/products/" + id + ".mp4");
        try {
            video.createNewFile();
            saveDataToFile(data, video);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateUniqueFileName() {
        Random random = new Random();
        return String.format("%s%s", System.currentTimeMillis(), random.nextInt(100000));
    }
}
