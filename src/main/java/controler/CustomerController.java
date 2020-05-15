package controler;

import ModelPackage.Log.PurchaseLog;
import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.CustomerInformation;
import ModelPackage.Users.SubCart;
import View.PrintModels.*;

import java.util.ArrayList;
import java.util.List;

public class CustomerController extends Controller {

    public CartPM viewCart(String username){
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
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription()
        );

        return new InCartPM(
                miniProductPM,
                subCart.getSeller().getUsername(),
                subCart.getAmount()
        );
    }

    public List<MiniProductPM> showProducts(String username){
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();
        List<MiniProductPM> miniProductPMS = new ArrayList<>();

        for (SubCart subCart : cart.getSubCarts()) {
            Product product = subCart.getProduct();
            MiniProductPM miniProductPM = new MiniProductPM(
                    product.getName(),
                    product.getId(),
                    product.getPrices(),
                    product.getCompany(),
                    product.getTotalScore(),
                    product.getDescription()
            );
            miniProductPMS.add(miniProductPM);
        }

        return miniProductPMS;
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

    public long showTotalPrice(String username){
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();

        return cart.getTotalPrice();
    }

    public void purchase(String username, String[] customerInfo, String disCode) throws NoSuchADiscountCodeException {
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

    public List<OrderMiniLogPM> viewOrders(String username){
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        List<PurchaseLog> purchaseLogs = customer.getPurchaseLogs();
        List<OrderMiniLogPM> orderMiniLogPMS = new ArrayList<>();

        for (PurchaseLog purchaseLog : purchaseLogs) {
            orderMiniLogPMS.add(createOrderMiniLog(purchaseLog));
        }

        return orderMiniLogPMS;
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
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription()
        );
    }

    public void rate(int logId, int rate){
        PurchaseLog purchaseLog = (PurchaseLog) csclManager.getLogById(logId);
        csclManager.rate(purchaseLog, rate);
    }

    public long viewBalance(String username){
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        return customer.getBalance();
    }

}
