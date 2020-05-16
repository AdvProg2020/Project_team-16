package controler;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Product;
import ModelPackage.System.SortType;
import ModelPackage.System.exeption.account.ProductNotHaveSellerException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import View.PrintModels.CommentPM;
import View.PrintModels.FullProductPM;
import View.PrintModels.MiniProductPM;
import controler.exceptions.NotAvailableSort;
import controler.exceptions.ProductsNotBelongToUniqueCategoryException;

import java.util.ArrayList;
import java.util.List;

public class ProductControler extends Controller{

    public void assignComment(String[] data) throws NoSuchAProductException {
        String userId = data[0];
        String commentTitle = data[1];
        String commentText = data[2];
        int productId = Integer.parseInt(data[3]);
        // TODO : check if user bought this product
        Comment comment = new Comment(userId, commentTitle, commentText,
                CommentStatus.NOT_VERIFIED, false);
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

    public FullProductPM[] compareProducts(String[] data)
            throws NoSuchAProductException, ProductsNotBelongToUniqueCategoryException {
        int firstProductId = Integer.parseInt(data[0]);
        int secondProductId = Integer.parseInt(data[1]);
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
        if (user.isHasSignedIn()) {
            int productId = Integer.parseInt(data[1]);
            //String[] toFindSellerData = new String[2];
            //toFindSellerData[0] = data[1];
            //toFindSellerData[1] = data[2];
            //String sellerUserName = selectSeller(toFindSellerData).getUsername();
            String sellerUserName = data[2];
            int amount = Integer.parseInt(data[3]);
            cartManager.addProductToCart(user.getCart(), sellerUserName, productId, amount);
        }
        else {
            accountManager.login(user.getUsername(), user.getPassword());
        }
    }

    public Seller selectSeller(String[] data) throws NoSuchAProductException,
            ProductNotHaveSellerException {
        int productId = Integer.parseInt(data[0]);
        String sellerUserName = data[1];
        return productManager.showSellerOfProduct(productId, sellerUserName);
    }

    public String[] showAvailableSorts() {
        String[] availableSort = new String[7];
        availableSort[0] = "Name";
        availableSort[1] = "Time";
        availableSort[2] = "View";
        availableSort[3] = "More Price";
        availableSort[4] = "Less Price";
        availableSort[5] = "Bought Amount";
        availableSort[6] = "Score";
        return availableSort;
    }

    public void sortProducts(String[] data) throws NotAvailableSort, NoSuchAProductException {
        String sortName = data[0];
        checkIfSortIsAvailable(sortName);
        ArrayList<Product> toSortProducts = new ArrayList<>();
        for (int i = 1; i < data.length; i++) {
            toSortProducts.add(productManager.findProductById(Integer.parseInt(data[i])));
        }
        SortType sortType = findSortType(data[0]);
        sortManager.sort(toSortProducts, sortType);
    }

    public void disableSortProducts() {
        sortManager.sort(sortManager.getList(), SortType.VIEW);
    }

    private SortType findSortType(String sortType) {
        switch (sortType) {
            case "Name" :
                return SortType.NAME;
            case "Time" :
                return SortType.TIME;
            case "View" :
                return SortType.VIEW;
            case "More Price" :
                return SortType.MORE_PRICE;
            case "Less Price" :
                return SortType.LESS_PRICE;
            case "Bought Amount" :
                return SortType.BOUGHT_AMOUNT;
            case "Score" :
                return SortType.SCORE;
        }
        return null;
    }

    private void checkIfSortIsAvailable(String sortName) throws NotAvailableSort {
        String[] availableSorts = showAvailableSorts();
        for (String sort : availableSorts) {
            if (!sort.equals(sortName))
                throw new NotAvailableSort(sortName);
        }
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
        return new MiniProductPM(product.getName(), product.getId(),
                product.getCategory().getName(),
                product.getPrices(), product.getCompany(),
                product.getTotalScore(), product.getDescription());
    }

}
