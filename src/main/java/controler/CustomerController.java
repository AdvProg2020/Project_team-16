package controler;

import ModelPackage.Log.PurchaseLog;
import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Off.Off;
import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.NoSuchACustomerException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
import ModelPackage.System.exeption.clcsmanager.NotABuyer;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.*;
import View.PrintModels.*;
import View.SortPackage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerController extends Controller {
    private static CustomerController customerController = new CustomerController();

    public static CustomerController getInstance() {
        return customerController;
    }

    public CartPM viewCart(String username) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();
        ArrayList<InCartPM> inCartPMS = new ArrayList<>();

        for (SubCart subCart : cart.getSubCarts()) {
            InCartPM inCartPM = createInCartPM(subCart);
            if (inCartPM != null) {
                inCartPMS.add(inCartPM);
            }
        }

        return new CartPM(
                cart.getTotalPrice(),
                inCartPMS
        );
    }

    private InCartPM createInCartPM(SubCart subCart) {
        Product product = subCart.getProduct();
        MiniProductPM miniProductPM = createMiniProductPMFrom(product);

        try {
            int price = findPriceForSpecialSeller(subCart.getSeller(), product);
            return new InCartPM(
                    miniProductPM,
                    subCart.getSeller().getUsername(),
                    price,
                    findOffPriceFor(subCart, price),
                    subCart.getAmount()
            );
        } catch (NoSuchSellerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InCartPM> showProducts(String username) throws UserNotAvailableException {
        ArrayList<InCartPM> inCartPMS = new ArrayList<>();
        Customer customer = DBManager.load(Customer.class,username);
        if (customer == null) {
            throw new UserNotAvailableException();
        }
        Cart cart = customer.getCart();

        for (SubCart subCart : cart.getSubCarts()) {
            inCartPMS.add(createInCartPM(subCart));
        }
        return inCartPMS;
    }

    public void changeAmount(String username, int id, int change)
            throws UserNotAvailableException, NoSuchAProductInCart,
            NoSuchAProductException, NoSuchSellerException,
            NotEnoughAmountOfProductException {
        Customer customer = DBManager.load(Customer.class,username);
        if (customer == null) {
            throw new UserNotAvailableException();
        }
        Cart cart = customer.getCart();
        String sellerId = cartManager.getSubCartByProductId(cart, id).getSeller().getUsername();
        cartManager.changeProductAmountInCart(cart, id, sellerId, change);
    }

    public void deleteProductFromCart(String username, int id)
            throws UserNotAvailableException, NoSuchAProductInCart {
        Customer customer = DBManager.load(Customer.class, username);
        if (customer == null) {
            throw new UserNotAvailableException();
        }
        cartManager.deleteProductFromCart(customer.getCart(), id);
    }

    public long showTotalPrice(String username) throws UserNotAvailableException {
        Customer customer = DBManager.load(Customer.class,username);
        if (customer == null) {
            throw new UserNotAvailableException();
        }
        Cart cart = customer.getCart();
        return cart.getTotalPrice();
    }

    public void purchase(String username, String[] customerInfo, String disCode)
            throws NoSuchADiscountCodeException, NotEnoughAmountOfProductException, NoSuchAProductException, NoSuchSellerException {
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

    public long getPurchaseTotalPrice(String discode, String username) throws NoSuchADiscountCodeException, UserNotAvailableException, NoSuchSellerException {
        DiscountCode discountCode = discountManager.getDiscountByCode(discode);
        Customer customer = (Customer) accountManager.getUserByUsername(username);
        return customerManager.getTotalPrice(discountCode, customer);
    }

    public List<OrderLogPM> viewOrders(String username) throws UserNotAvailableException, NoSuchALogException, NoSuchAProductException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        List<PurchaseLog> purchaseLogs = customer.getPurchaseLogs();
        List<OrderLogPM> orderMiniLogPMS = new ArrayList<>();

        for (PurchaseLog purchaseLog : purchaseLogs) {
            orderMiniLogPMS.add(showOrder(purchaseLog.getLogId()));
        }

        return orderMiniLogPMS;
    }

    private OrderLogPM showOrder(int id) throws NoSuchAProductException, NoSuchALogException {
        PurchaseLog purchaseLog = (PurchaseLog) csclManager.getLogById(id);
        ArrayList<OrderProductPM> orderProductPMS = createOrderProductPM(purchaseLog);

        float realPrice = (float) purchaseLog.getPricePaid() / (1 + (float)purchaseLog.getDiscount()/100);

        return new OrderLogPM(
                purchaseLog.getLogId(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(purchaseLog.getDate()),
                orderProductPMS,
                purchaseLog.getDeliveryStatus().toString(),
                (long)realPrice,
                purchaseLog.getPricePaid(),
                purchaseLog.getDiscount()
        );
    }

    private ArrayList<OrderProductPM> createOrderProductPM(PurchaseLog purchaseLog) throws NoSuchAProductException {
        List<SoldProductSellerMap> productSellerMaps = purchaseLog.getProductsAndItsSellers();
        ArrayList<OrderProductPM> orderProductPMS = new ArrayList<>();

        for (SoldProductSellerMap productSellerMap : productSellerMaps) {
            int id = productSellerMap.getSoldProduct().getSourceId();
            Product product = productManager.findProductById(id);
            orderProductPMS.add(createOrderProductPMFrom(product, productSellerMap.getSeller()));
        }

        return orderProductPMS;
    }

    private OrderProductPM createOrderProductPMFrom(Product product, Seller seller) {
        OrderProductPM orderProductPM = null;

        for (SellPackage aPackage : product.getPackages()) {
            if (aPackage.getSeller().equals(seller)){
                orderProductPM = new OrderProductPM(product.getId(), product.getName(), seller.getUsername(), aPackage.getPrice());
            }
        }

        return orderProductPM;
    }

    private int findOffPriceFor(SubCart subCart, int price) throws NoSuchSellerException {
        Off off = subCart.getProduct().findPackageBySeller(subCart.getSeller().getUsername()).getOff();
        if (off != null) {
            return price * (100 - off.getOffPercentage()) / 100;
        }
        else return 0;
    }

    private int findPriceForSpecialSeller(Seller seller, Product product) throws NoSuchSellerException {
        return product.findPackageBySeller(seller).getPrice();
    }

    public long viewBalance(String username) throws UserNotAvailableException {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        return customer.getBalance();
    }

    public List<DisCodeUserPM> viewDiscountCodes(String username, SortPackage sortPackage) throws UserNotAvailableException {
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

    public void assignAScore(String username,int productId,int score) throws NoSuchAProductException, NotABuyer, NoSuchACustomerException {
        csclManager.createScore(username,productId,score);
    }

    private MiniProductPM createMiniProductPMFrom(Product product){
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
}
