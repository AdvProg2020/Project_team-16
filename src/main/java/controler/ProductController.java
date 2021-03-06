package controler;

import ModelPackage.Product.*;
import ModelPackage.System.FilterManager;
import ModelPackage.System.exeption.account.NoSuchACustomerException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.filters.InvalidFilterException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.User;
import View.FilterPackage;
import View.PrintModels.*;
import View.SortPackage;
import controler.exceptions.ProductsNotBelongToUniqueCategoryException;
import javafx.scene.image.Image;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductController extends Controller{
    private static ProductController productController = new ProductController();

    public static ProductController getInstance() {
        return productController;
    }

    public List<MiniProductPM> showAllProducts(SortPackage sortPackage, FilterPackage filterPackage) throws NoSuchACategoryException, InvalidFilterException {
        int[] priceRange = {filterPackage.getDownPriceLimit(),filterPackage.getUpPriceLimit()};
        List<Product> products = FilterManager.updateFilterList(filterPackage.getCategoryId(),
                filterPackage.getActiveFilters(), priceRange, filterPackage.isOffMode(), filterPackage.isAvaiOnly());
        products = filterProductByFields(products, filterPackage);
        sortManager.sort(products, sortPackage.getSortType());
        if (!sortPackage.isAscending()) Collections.reverse(products);
        List<MiniProductPM> toReturn = new ArrayList<>();
        for (Product product : products) {
            toReturn.add(createMiniProductPM(product));
        }
        return toReturn;
    }

    private List<Product> filterProductByFields(List<Product> products, FilterPackage filterPackage) {
        String name = filterPackage.getName();
        String seller = filterPackage.getSeller();
        String brand = filterPackage.getBrand();
        List<Product> list = new CopyOnWriteArrayList<>(products);
        if (name != null)
            for (Product product : list) {
                if (!product.getName().toLowerCase().contains(name.toLowerCase())) list.remove(product);
            }
        if (seller != null)
            for (Product product : list) {
                if (!hasSeller(product, seller)) list.remove(product);
            }
        if (brand != null)
            for (Product product : list) {
                if (!product.getCompanyClass().getName().toLowerCase().contains(brand.toLowerCase()))
                    list.remove(product);
            }
        return list;
    }

    private List<SellPackage> filterSellPackagesField(List<SellPackage> sellPackages, FilterPackage filterPackage) {
        String name = filterPackage.getName();
        String seller = filterPackage.getSeller();
        String brand = filterPackage.getBrand();
        List<SellPackage> list = new CopyOnWriteArrayList<>(sellPackages);
        if (name != null)
            for (SellPackage product : list) {
                if (!product.getProduct().getName().toLowerCase().contains(name.toLowerCase())) list.remove(product);
            }
        if (seller != null)
            for (SellPackage product : list) {
                if (!product.getSeller().getUsername().toLowerCase().contains(seller.toLowerCase()))
                    list.remove(product);
            }
        if (brand != null)
            for (SellPackage product : list) {
                if (!product.getProduct().getCompanyClass().getName().toLowerCase().contains(brand.toLowerCase()))
                    list.remove(product);
            }
        return list;
    }

    private boolean hasSeller(Product product, String seller) {
        for (SellPackage aPackage : product.getPackages()) {
            if (aPackage.getSeller().getUsername().toLowerCase().contains(seller.toLowerCase())) return true;
        }
        return false;
    }

    public List<OffProductPM> showAllOnOffProducts(FilterPackage filter, SortPackage sort) {
        List<SellPackage> allSellPackagesOnOff = offManager.getAllSellPackagesOnOff();
        List<SellPackage> filtered = FilterManager.filterSellPackages(filter.getCategoryId(), allSellPackagesOnOff,
                filter.getActiveFilters(),
                new int[]{filter.getDownPriceLimit(), filter.getUpPriceLimit()}, filter.isAvaiOnly());
        filtered = filterSellPackagesField(filtered, filter);
        sortManager.sortSellPackage(filtered, sort.getSortType());
        if (!sort.isAscending()) Collections.reverse(filtered);
        List<OffProductPM> toReturn = new ArrayList<>();
        filtered.forEach(sellPackage -> {
            OffProductPM offProductPM = createOffPM(sellPackage);
            toReturn.add(offProductPM);
        });
        return toReturn;
    }

    private OffProductPM createOffPM(SellPackage sellPackage) {
        int price = sellPackage.getPrice();
        int percent = sellPackage.getOff().getOffPercentage();
        String name = sellPackage.getProduct().getName();
        int id = sellPackage.getProduct().getId();
        Image image = ProductController.getInstance().loadMainImage(id);
        Date end = sellPackage.getOff().getEndTime();
        return new OffProductPM(name, id, price * (100 - percent) / 100, percent, image, end);
    }

    public void assignComment(String[] data) throws NoSuchAProductException, NoSuchACustomerException, UserNotAvailableException {
        String userId = data[0];
        String commentTitle = data[1];
        String commentText = data[2];
        int productId = Integer.parseInt(data[3]);
        Comment comment = new Comment(userId, commentTitle, commentText,
                CommentStatus.NOT_VERIFIED,
                csclManager.boughtThisProduct(productId, userId));
        comment.setProduct(productManager.findProductById(productId));
        csclManager.createComment(comment);
    }

    public List<CommentPM> viewProductComments(int productId) throws NoSuchAProductException {
        Comment[] comments = productManager.showComments(productId);
        List<CommentPM> commentPMs = new ArrayList<>();
        for (Comment comment : comments) {
            commentPMs.add(new CommentPM(comment.getUserId(),
                    comment.getTitle(),
                    comment.getText(),
                    comment.isBoughtThisProduct()));
        }
        return commentPMs;
    }

    public FullProductPM viewAttributes(int productId) throws NoSuchAProductException {
        return createFullProductPM(productId);
    }

    public FullProductPM[] compareProducts(int[] data)
            throws NoSuchAProductException, ProductsNotBelongToUniqueCategoryException {
        int firstProductId = data[0];
        int secondProductId = data[1];
        checkIfTwoProductsDoesNotBelongToUniqueCategory(firstProductId, secondProductId);
        FullProductPM[] fullProductPMs = new FullProductPM[2];
        fullProductPMs[0] = createFullProductPM(firstProductId);
        fullProductPMs[1] = createFullProductPM(secondProductId);
        return fullProductPMs;
    }

    public MiniProductPM digest(int productId) throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        return createMiniProductPM(product);
    }

    public void addToCart(String[] data) throws Exception {
        String userName = data[0];
        User user = accountManager.getUserByUsername(userName);
        int productId = Integer.parseInt(data[1]);
        String sellerUserName = data[2];
        int amount = Integer.parseInt(data[3]);
        cartManager.addProductToCart(user.getCart(), sellerUserName, productId, amount);
    }

    private void checkIfTwoProductsDoesNotBelongToUniqueCategory(int firstProductId, int secondProductId)
            throws ProductsNotBelongToUniqueCategoryException, NoSuchAProductException {
        Product firstProduct = productManager.findProductById(firstProductId);
        Product secondProduct = productManager.findProductById(secondProductId);
        if (firstProduct.getId() != (secondProduct.getId()))
            throw new ProductsNotBelongToUniqueCategoryException(firstProduct.getId(), secondProduct.getId());
    }

    private FullProductPM createFullProductPM(int productId) throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        return new FullProductPM(createMiniProductPM(product),
                productManager.allFeaturesOf(product));
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
                sellPackagePMs, product.isAvailable());
    }

    public List<MiniProductPM> showOffs(SortPackage sortPackage,FilterPackage filterPackage){
        List<Product> products = productManager.getAllOffFromActiveProducts();
        int[] priceRange = {filterPackage.getDownPriceLimit(),filterPackage.getUpPriceLimit()};
        List<Product> filtered = FilterManager.filterList(products,filterPackage.getActiveFilters(),priceRange);
        sortManager.sort(filtered,sortPackage.getSortType());
        if (!sortPackage.isAscending())Collections.reverse(filtered);
        List<MiniProductPM> toReturn = new ArrayList<>();
        filtered.forEach(product -> toReturn.add(createMiniProductPM(product)));
        return toReturn;
    }

    public ArrayList<MicroProduct> findSimilarProductsByName(String name){
        return productManager.findProductByName(name);
    }

    public List<MicroProduct> getAllProductInCategoryByProductId(int id) throws NoSuchACategoryException, NoSuchAProductException {
        Product product = productManager.findProductById(id);
        return categoryManager.allProductsInACategoryList(product.getCategory().getId());
    }

    public ArrayList<Image> loadImage(int id) {
        ArrayList<Image> images = new ArrayList<>();
        File mainImageFile = new File("src/main/resources/db/images/products/" + id + "/main.jpg");
        File otherImagesDirectory = new File("src/main/resources/db/images/products/" + id + "/other");
        Image main = createImageFromFile(mainImageFile);
        if (main != null)
            images.add(main);
        File[] otherImages = otherImagesDirectory.listFiles();
        if (otherImages != null) {
            List<File> files = Arrays.asList(otherImages);
            files.forEach(file -> {
                Image image = createImageFromFile(file);
                if (image != null)
                    images.add(image);
            });
        }
        return images;
    }

    public Image loadMainImage(int id) {
        File mainImageFile = new File("src/main/resources/db/images/products/" + id + "/main.jpg");
        return createImageFromFile(mainImageFile);
    }

    private Image createImageFromFile(File file) {
        if (file.exists()) {
            return new Image(String.valueOf(file.toURI()));
        }
        return null;
    }
}
