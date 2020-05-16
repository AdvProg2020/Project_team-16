package controler;

import ModelPackage.Log.PurchaseLog;
import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Off.Off;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.*;
import View.PrintModels.*;

import java.util.ArrayList;
import java.util.List;

public class CustomerController extends Controller {

    public CartPM viewCart(String username) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();
        ArrayList<InCartPM> inCartPMS = new ArrayList<>();

        for (SubCart subCart : cart.getSubCarts()) {
            inCartPMS.add(createInCartPM(subCart));
        }

        return new CartPM(
                cart.getTotalPrice(),
                inCartPMS
        );
    }

    private InCartPM createInCartPM(SubCart subCart){
        Product product = subCart.getProduct();
        MiniProductPM miniProductPM = new MiniProductPM(
                product.getName(),
                product.getId(),
                product.getCategory().getName(),
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription()
        );

        return new InCartPM(
                miniProductPM,
                subCart.getSeller().getUsername(),
                findPriceForSpecialSeller(subCart.getSeller(), product) * subCart.getAmount(),
                (int) findOffPriceFor(subCart),
                subCart.getAmount()
        );
    }

    public List<InCartPM> showProducts(String username) throws UserNotAvailableException {
        ArrayList<InCartPM> inCartPMS = new ArrayList<>();

        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();

        for (SubCart subCart : cart.getSubCarts()) {
            inCartPMS.add(createInCartPM(subCart));
        }

        return inCartPMS;
    }

    public Product viewProduct(int id) throws NoSuchAProductException {
        return productManager.findProductById(id);
    }

    public void changeAmount(String username, int id, int change) throws Exception {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();
        String sellerId = cartManager.getSubCartByProductId(cart, id).getSeller().getUsername();
        cartManager.changeProductAmountInCart(cart, id, sellerId, change);
    }

    public long showTotalPrice(String username) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();

        return cart.getTotalPrice();
    }

    public void purchase(String username, String[] customerInfo, String disCode)
            throws NoSuchADiscountCodeException, NotEnoughAmountOfProductException, NoSuchAProductException {
        CustomerInformation customerInformation = new CustomerInformation(
                customerInfo[0],
                customerInfo[1],
                customerInfo[2],
                customerInfo[3]
        );
        DiscountCode discountCode = discountManager.getDiscountByCode(disCode);

        customerManager.purchase(
                username,
                customerInformation,
                discountCode
        );
    }

    public List<OrderMiniLogPM> viewOrders(String username) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        List<PurchaseLog> purchaseLogs = customer.getPurchaseLogs();
        List<OrderMiniLogPM> orderMiniLogPMS = new ArrayList<>();

        for (PurchaseLog purchaseLog : purchaseLogs) {
            orderMiniLogPMS.add(createOrderMiniLog(purchaseLog));
        }

        return orderMiniLogPMS;
    }

    private double findOffPriceFor(SubCart subCart) {
        Off off = subCart.getProduct().getOff();
        if (off == null)
            return 0;
        return findPriceForSpecialSeller(subCart.getSeller(), subCart.getProduct()) -
                (double)(off.getOffPercentage() / 100) *
                        findPriceForSpecialSeller(subCart.getSeller(), subCart.getProduct());
    }

    private int findPriceForSpecialSeller(Seller seller, Product product) {
        for (SellerIntegerMap sellerIntegerMap : product.getPrices()) {
            if (sellerIntegerMap.getSeller().equals(seller))
                return sellerIntegerMap.getInteger();
        }
        return 0;
    }

    private OrderMiniLogPM createOrderMiniLog(PurchaseLog purchaseLog){
        return new OrderMiniLogPM(
                purchaseLog.getDate(),
                purchaseLog.getLogId(),
                purchaseLog.getPricePaid()
        );
    }

    public OrderLogPM showOrder(int id) throws NoSuchAProductException {
        PurchaseLog purchaseLog = (PurchaseLog) csclManager.getLogById(id);
        ArrayList<MiniProductPM> miniProductPMS = createMiniProductPM(purchaseLog);

        float realPrice = (float) purchaseLog.getPricePaid() / (1 + (float)purchaseLog.getDiscount()/100);

        return new OrderLogPM(
                purchaseLog.getDate(),
                miniProductPMS,
                purchaseLog.getDeliveryStatus().toString(),
                (long)realPrice,
                purchaseLog.getPricePaid(),
                purchaseLog.getDiscount()
        );
    }

    private ArrayList<MiniProductPM> createMiniProductPM(PurchaseLog purchaseLog) throws NoSuchAProductException {
        List<SoldProductSellerMap> productSellerMaps = purchaseLog.getProductsAndItsSellers();
        ArrayList<MiniProductPM> miniProductPMS = new ArrayList<>();

        for (SoldProductSellerMap productSellerMap : productSellerMaps) {
            int id = productSellerMap.getSoldProduct().getSourceId();
            Product product = productManager.findProductById(id);
            miniProductPMS.add(addMiniProductPM(product));
        }

        return miniProductPMS;
    }

    private MiniProductPM addMiniProductPM(Product product){
        return new MiniProductPM(
                product.getName(),
                product.getId(),
                product.getCategory().getName(),
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription()
        );
    }

    public long viewBalance(String username) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        return customer.getBalance();
    }

    public List<DisCodeUserPM> viewDiscountCodes(String username) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        List<DiscountcodeIntegerMap> discountCodes = customer.getDiscountCodes();
        ArrayList<DisCodeUserPM> disCodeUserPMS = new ArrayList<>();

        for (DiscountcodeIntegerMap discountCode : discountCodes) {
            disCodeUserPMS.add(createDiscountPM(discountCode));
        }

        return disCodeUserPMS;
    }

    private DisCodeUserPM createDiscountPM(DiscountcodeIntegerMap discountcodeIntegerMap){
        DiscountCode discountCode = discountcodeIntegerMap.getDiscountCode();
        return new DisCodeUserPM(
                discountCode.getCode(),
                discountCode.getStartTime(),
                discountCode.getEndTime(),
                discountCode.getOffPercentage(),
                discountCode.getMaxDiscount(),
                discountcodeIntegerMap.getInteger()
        );
    }
}
