package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.SellLog;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import ModelPackage.Users.User;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SellerManagerTest {
    private SellerManager sellerManager;
    private Seller marmof;
    private User hatam;
    private Company adidas;
    private SellLog dullSellLog;
    private SellLog skirtSellLog;
    private Product dullForKimmi;
    private Product skirtForKimmi;
    private List<SellLog> sellLogs;
    private List<Product> products;
    private Category category;
    private Cart cart;
    private SubCart skirt;

    {
        adidas = new Company("Adidas","115", "Clothing",new ArrayList<>());

        sellerManager = SellerManager.getInstance();

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
        hatam = new User(
                "hatam008",
                "hatapass",
                "Ali",
                "Hatami",
                "hatam008@gmail.com",
                "+98 992 1122",
                new Cart()
        );

        SellPackage dullSellPackage = null;
        dullForKimmi = new Product("dullForKimmi", adidas, new Category(), new HashMap<>(),
                new HashMap<>(), "Awesome", dullSellPackage);
        dullSellPackage = new SellPackage(dullForKimmi, marmof, 10000, 6, null, false, true);
        dullForKimmi.setId(1);

        /*List<SellerIntegerMap> prices = new ArrayList<>();

        SellerIntegerMap price = new SellerIntegerMap();
        price.setInteger(10000);
        price.setSeller(marmof);

        dullForKimmi.setPrices(prices);*/

        SellPackage skirtSellPackage = new SellPackage(skirtForKimmi, marmof, 10000, 3, null, false, true);
        skirtForKimmi = new Product("skirtForKimmi", adidas, new Category(), new HashMap<>(),
                new HashMap<>(), "Awesome", skirtSellPackage);
        skirtForKimmi.setId(2);

        products = new ArrayList<>();
        products.add(dullForKimmi);
        /*products.add(skirtForKimmi);*/

        /*dullForKimmi.setAllSellers(new ArrayList<>());
        dullForKimmi.getAllSellers().add(marmof);*/

        SoldProduct soldDull = new SoldProduct();
        soldDull.setSoldPrice(10000);
        soldDull.setSourceId(dullForKimmi.getId());

        SoldProduct soldSkirt = new SoldProduct();
        soldDull.setSoldPrice(30000);
        soldDull.setSourceId(skirtForKimmi.getId());

        dullSellLog = new SellLog(
                soldDull,
                100000,
                5,
                hatam,
                new Date(),
                DeliveryStatus.DELIVERED
        );
        skirtSellLog = new SellLog(
                soldSkirt,
                300000,
                7,
                hatam,
                new Date(),
                DeliveryStatus.DELIVERED
        );

        sellLogs = new ArrayList<>();
        sellLogs.add(dullSellLog);
        sellLogs.add(skirtSellLog);

        marmof.setSellLogs(sellLogs);

        //marmof.setProducts(products);
        List<SellPackage> marmofSellPackages = new ArrayList<>();
        marmofSellPackages.add(dullSellPackage);
        marmof.setPackages(marmofSellPackages);

        category = new Category("stuffForKimmi",null);

        cart = new Cart();
        skirt = new SubCart(skirtForKimmi, marmof,3);
        cart.getSubCarts().add(skirt);

        FakeDBManager fakeDBManager = new FakeDBManager();
        fakeDBManager.save(marmof);


    }


    @Before
    public void create() {

    }
    @Test
    public void viewCompanyInformation() throws UserNotAvailableException {
        Company actualCompany = sellerManager.viewCompanyInformation("marmofayezi");
        assertEquals(adidas,actualCompany);
    }

    @Test(expected = UserNotAvailableException.class)
    public void viewCompany_NotAvailableTest() throws UserNotAvailableException {
        sellerManager.viewCompanyInformation("sapa");
    }

    @Test
    public void viewSalesHistory() throws UserNotAvailableException {
        List<SellLog> actualSellLogs = sellerManager.viewSalesHistory("marmofayezi");
        assertArrayEquals(sellLogs.toArray(),actualSellLogs.toArray());
    }
    @Test(expected = UserNotAvailableException.class)
    public void viewSaleHistory_NotAvailableTest() throws UserNotAvailableException {
        sellerManager.viewSalesHistory("sapa");
    }

    @Test
    public void viewProducts() throws UserNotAvailableException {
        List<Product> actualProducts = sellerManager.viewProducts("marmofayezi");
        assertArrayEquals(products.toArray(),actualProducts.toArray());
    }

    @Test(expected = UserNotAvailableException.class)
    public void viewProducts_NotAvailableTest() throws UserNotAvailableException {
        sellerManager.viewProducts("sapa");
    }

    @Test
    public void viewSellersOfProduct() throws NoSuchAProductException {
        new MockUp<ProductManager>(){
            @Mock
            public Product findProductById(int id){
                return skirtForKimmi;
            }
        };
        List<Seller> actualSellers = sellerManager.viewSellersOfProduct(skirtForKimmi.getId());
        List<Seller> expectedSellers = new ArrayList<>();
        expectedSellers.add(marmof);
        assertArrayEquals(expectedSellers.toArray(), actualSellers.toArray());
    }

    @Test
    public void getMoneyFromSale() throws NoSuchAPackageException {
        new MockUp<DBManager>(){
            @Mock
            public void save(Object o) {
            }
        };
        new MockUp<CSCLManager>(){
            @Mock
            int findPrice(SubCart subCart){
                if (subCart.getAmount() == 5){
                    return 10000;
                } else {
                    return 30000;
                }
            }
        };
        sellerManager.getMoneyFromSale(cart);
        assertEquals(90000,marmof.getBalance());
    }

    @Test
    public void addSellLog() {
        sellerManager.addASellLog(dullSellLog, marmof);
        sellerManager.addASellLog(skirtSellLog, marmof);
        assertArrayEquals(sellLogs.toArray(), marmof.getSellLogs().toArray());
    }

    @Test
    public void deleteProductFromSellerTest() throws NoSuchAPackageException {
        new MockUp<DBManager>() {
            public void delete(Object object){}
        };
        sellerManager.deleteProductForSeller(marmof.getUsername(), 1);
        assertEquals(marmof.getPackages().size(), 0);
    }
}
