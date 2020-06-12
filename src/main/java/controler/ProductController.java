package controler;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Off.Off;
import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Product;
import ModelPackage.System.FilterManager;
import ModelPackage.System.exeption.account.NoSuchACustomerException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.filters.InvalidFilterException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import View.FilterPackage;
import View.PrintModels.*;
import View.SortPackage;
import controler.exceptions.ProductsNotBelongToUniqueCategoryException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductController extends Controller{
    private static ProductController productController = new ProductController();

    public static ProductController getInstance() {
        return productController;
    }

    public List<MiniProductPM> showAllProducts(SortPackage sortPackage, FilterPackage filterPackage) throws NoSuchACategoryException, InvalidFilterException {
        int[] priceRange = {filterPackage.getDownPriceLimit(),filterPackage.getUpPriceLimit()};
        ArrayList<Product> products = FilterManager.updateFilterList(filterPackage.getCategoryId(), filterPackage.getActiveFilters(), priceRange);
        sortManager.sort(products,sortPackage.getSortType());
        if (!sortPackage.isAscending()) Collections.reverse(products);
        List<MiniProductPM> toReturn = new ArrayList<>();
        for (Product product : products) {
            toReturn.add(createMiniProductPM(product));
        }
        return toReturn;
    }

    public void assignComment(String[] data) throws NoSuchAProductException, NoSuchACustomerException {
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
                    comment.getText()));
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
                sellPackagePMs);
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
}
