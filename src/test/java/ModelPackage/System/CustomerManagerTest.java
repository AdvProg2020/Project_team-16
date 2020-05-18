package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.PurchaseLog;
import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Product.SoldProduct;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.NotEnoughMoneyException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.*;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

public class CustomerManagerTest {
    private CustomerManager customerManager;
    private Customer hatam;
    private PurchaseLog dullForKimmiLog;
    private Product dullForKimmi;
    private Seller marmof;
    private Company adidas;
    private List<PurchaseLog> purchaseLogs;
    private DiscountCode discountCode;
    ArrayList<DiscountcodeIntegerMap> discounts;
    CustomerInformation customerInformation;
    Cart hatamCart;

    {
        customerManager = CustomerManager.getInstance();

        hatam = new Customer(
                "hatam008",
                "hatam009",
                "Ali",
                "Hatami",
                "hatam008@gmail.com",
                "0912 133 1232",
                new Cart(),
                20000
        );

        adidas = new Company("Adidas","115", "Clothing",new ArrayList<>());
        marmof = new Seller(
                "marmofayezi",
                "marmof.ir",
                "Cyrus",
                "Statham",
                "marmof@gmail.com",
                "+1 992 1122",
                new Cart(),
                adidas,
                0
        );
        dullForKimmi = new Product();

        SoldProductSellerMap productSeller = new SoldProductSellerMap();
        productSeller.setSoldProduct(new SoldProduct());
        productSeller.setSeller(marmof);

        ArrayList<SoldProductSellerMap> productSellerMaps = new ArrayList<>();
        productSellerMaps.add(productSeller);
        dullForKimmiLog = new PurchaseLog(
                new Date(),
                DeliveryStatus.DELIVERED,
                productSellerMaps,
                10000,
                9
        );

        purchaseLogs = new ArrayList<>();
        purchaseLogs.add(dullForKimmiLog);

        hatam.setPurchaseLogs(purchaseLogs);

        discountCode = new DiscountCode("Dis12", new Date(), new Date(), 10, 10000);

        DiscountcodeIntegerMap discount = new DiscountcodeIntegerMap();
        discount.setDiscountCode(discountCode);
        discount.setInteger(3);

        discounts = new ArrayList<>();
        discounts.add(discount);

        hatam.setDiscountCodes(discounts);

        customerInformation = new CustomerInformation(
                "tehran",
                "10100",
                "123456",
                "45233"
        );

        hatamCart = new Cart();
        hatamCart.setDiscountCode(discountCode.getCode());

        SubCart subCart = new SubCart(dullForKimmi, marmof, 2);
        List<SubCart> subCarts = new ArrayList<>();
        subCarts.add(subCart);

        hatamCart.setSubCarts(subCarts);
        hatamCart.setTotalPrice(20000);

        hatam.setCart(hatamCart);

        SellerIntegerMap sellerIntegerMap = new SellerIntegerMap();
        sellerIntegerMap.setInteger(10);

        ArrayList<SellerIntegerMap> stock = new ArrayList<>();
        stock.add(sellerIntegerMap);

        dullForKimmi.setStock(stock);
    }

    @Before
    public void initialize(){
        new MockUp<AccountManager>(){
            @Mock
            public User getUserByUsername(String username) throws UserNotAvailableException {
                if (username.equals("marmofayezi")) return marmof;
                else if (username.equals("hatam008")) return hatam;
                else throw new UserNotAvailableException();
            }
        };
        new MockUp<DBManager>(){
            @Mock
            public void save(Object o) {
            }
            @Mock
            public <T> T load(Class<T> type, Serializable username) throws UserNotAvailableException {
                if (username.equals("marmofayezi") && type.getName().equals("ModelPackage.Users.Seller")) return (T) marmof;
                else if (username.equals("hatam008") && type.getName().equals("ModelPackage.Users.Customer")) return (T) hatam;
                else return null;
            }
        };
    }


    @Test
    public void viewOrders(){
        List<PurchaseLog> actualPurchaseLog = customerManager.viewOrders("hatam008");

        Assert.assertArrayEquals(purchaseLogs.toArray(),actualPurchaseLog.toArray());
    }

    @Test
    public void viewDisCodes(){
        List<DiscountcodeIntegerMap> actualDiscodes = customerManager.viewDiscountCodes("hatam008");

        Assert.assertArrayEquals(discounts.toArray(),actualDiscodes.toArray());
    }

    @Test
    public void purchase() throws NotEnoughAmountOfProductException, NoSuchAProductException {
        new MockUp<CustomerManager>(){
            @Mock
            public void checkIfThereIsEnoughAmount(Customer customer){}
            @Mock
            public void purchaseForCustomer(Customer customer, CustomerInformation customerInformation, DiscountCode discountCode){}
            @Mock
            public void productChangeInPurchase(Customer customer){}
        };
        new MockUp<CSCLManager>(){
            @Mock
            public void createPurchaseLog(Cart cart, int discount,Customer customer){}
        };
        new MockUp<SellerManager>(){
            @Mock
            public void getMoneyFromSale(Cart cart){
                marmof.setBalance(marmof.getBalance() + 20000);
            }
        };

        customerManager.purchase("hatam008", customerInformation, discountCode);

        Assert.assertEquals(20000, marmof.getBalance());
    }

    @Test
    public void checkEnoughAmount() throws NotEnoughAmountOfProductException, NoSuchAProductException {
        new MockUp<CartManager>(){
            @Mock
            public void checkIfThereIsEnoughAmountOfProduct(int productId, String sellerId, int amount){}
        };
        customerManager.checkIfThereIsEnoughAmount(hatam);
    }

    @Test
    public void purchaseForCustomer() {
        customerManager.purchaseForCustomer(hatam, customerInformation, discountCode);

        Assert.assertEquals(2000, hatam.getBalance());
    }

    @Test
    public void getTotalPrice() {
        long actual = customerManager.getTotalPrice(discountCode, hatam);
        long expected = 18000;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void productChangeInPurchase() {
        new MockUp<ProductManager>(){
            @Mock
            public void changeAmountOfStock(int productId, String sellerId, int amount){
                dullForKimmi.getStock().get(0).setInteger(8);
            }
        };

        customerManager.productChangeInPurchase(hatam);

        int expected = 8;
        int actual = dullForKimmi.getStock().get(0).getInteger();

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void checkIfCustomerHasEnoughMoney(){
        customerManager.checkIfCustomerHasEnoughMoney(10);
    }
}
