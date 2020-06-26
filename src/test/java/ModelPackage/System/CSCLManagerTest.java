package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.Log;
import ModelPackage.Log.PurchaseLog;
import ModelPackage.Log.SellLog;
import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.NoSuchACustomerException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.clcsmanager.NoSuchACompanyException;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
import ModelPackage.System.exeption.clcsmanager.NotABuyer;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import ModelPackage.Users.*;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class CSCLManagerTest {
    private final CSCLManager csclManager;
    private final Company adidas;
    private final Company puma;
    private final Product product;
    private Product shoe;
    private final Cart cart;
    private final SubCart subCart;
    private final Comment comment;
    private final SellLog sellLog;
    private final Customer sapa;
    private final Seller sajad;
    {
        csclManager = CSCLManager.getInstance();
        adidas = new Company("Adidas", "34524532", "Sports");
        puma = new Company("Puma", "12434565", "Sports");
        Company nike = new Company("Nike", "2123435", "Sports");

        sajad = new Seller("sapa", "1234", "sajad", "paksima", "sajad@gmail.com",
                "12345", new Cart(), adidas, 12);

        SellPackage shoeSellPackage = new SellPackage(shoe, sajad, 1000, 4, null, false, true);

        shoe = new Product("Shoes", adidas, new Category(),
                new HashMap<>(), new HashMap<>(),
                "good", shoeSellPackage);
        shoe.setId(1);

        product = new Product();
        product.setId(12);

        subCart = new SubCart(shoe, sajad, 1);
        cart = new Cart();
        cart.getSubCarts().add(subCart);

        comment = new Comment("sapa",
                "very Good",
                "Awesome !",
                CommentStatus.VERIFIED,
                true);
        comment.setProduct(product);

        SoldProductSellerMap soldProductSellerMap = new SoldProductSellerMap();
        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setSourceId(1);
        soldProductSellerMap.setSoldProduct(soldProduct);
        soldProductSellerMap.setSeller(sajad.getUsername());
        List<SoldProductSellerMap> soldProductSellerMaps = new ArrayList<>();
        soldProductSellerMaps.add(soldProductSellerMap);

        sapa = new Customer("sapa", "12434", "Ali", "Alavi",
                "alavi@gmail.com", "123435", new Cart(), 21);
        sapa.getPurchaseLogs().add(new PurchaseLog(new Date(2014, Calendar.MARCH, 3),
                DeliveryStatus.DELIVERED, soldProductSellerMaps, 21, 1));

        sellLog = new SellLog();
        sellLog.setLogId(1);
        FakeDBManager fakeDBManager = new FakeDBManager();
        fakeDBManager.save(sellLog);
        fakeDBManager.save(puma);
        fakeDBManager.save(nike);
        fakeDBManager.save(sapa);
        fakeDBManager.save(shoe);
    }
    @Test
    public void getInstanceTest() {
        CSCLManager test = CSCLManager.getInstance();
        assertEquals(test, csclManager);
    }
    @Test
    public void createCompanyTest() {
        int id = csclManager.createCompany(adidas);
        assertEquals(adidas, csclManager.getCompanyById(id));
    }

    @Test(expected = NoSuchACompanyException.class)
    public void getCompanyByIdNotFountExcTest() {
        csclManager.getCompanyById(adidas.getId());
    }

    @Test
    public void editCompanyNameTest() {
        int id = csclManager.createCompany(adidas);
        csclManager.editCompanyName(id, "adidas");
        assertEquals("adidas", adidas.getName());
    }
    @Test(expected = NoSuchACompanyException.class)
    public void editCompanyNameNotFoundExcTest() {
        csclManager.editCompanyName(adidas.getId(), "Adidas");
    }
    @Test
    public void editCompanyGroupTest() {
        csclManager.createCompany(adidas);
        csclManager.editCompanyGroup(adidas.getId(), "Sporty");
        assertEquals("Sporty", adidas.getGroup());
    }
    @Test(expected = NoSuchACompanyException.class)
    public void editCompanyGroupNotFoundExcTest() {
        csclManager.editCompanyGroup(adidas.getId(), "Sports");
    }
    @Test
    public void addProductToCompanyTest(@Mocked ProductManager productManager)
            throws NoSuchAProductException {
        new Expectations(){{
             productManager.findProductById(12);
             result = product;
        }};
        csclManager.createCompany(adidas);
        csclManager.addProductToCompany(12, adidas.getId());
        assertEquals(adidas.getProductsIn().get(adidas.getProductsIn().size() - 1), product);
    }
    @Test(expected = NoSuchACompanyException.class)
    public void addProductToCartNotFoundComExcTest()
            throws NoSuchAProductException {
        csclManager.addProductToCompany(12, adidas.getId());
    }
    @Test(expected = NoSuchAProductException.class)
    public void addProductToCartNotFoundProExcTest()
            throws NoSuchAProductException {
        csclManager.createCompany(adidas);
        csclManager.addProductToCompany(13, adidas.getId());
    }
    @Test
    public void removeProductFromCompanyTest(@Mocked ProductManager productManager)
            throws NoSuchAProductException {
        new Expectations(){{
            productManager.findProductById(12);
            result = product;
        }};
        csclManager.createCompany(adidas);
        csclManager.addProductToCompany(12, adidas.getId());
        int oldSize = adidas.getProductsIn().size();
        csclManager.removeProductFromCompany(12, adidas.getId());
        int newSize = puma.getProductsIn().size();
        assertEquals(newSize + 1, oldSize);
    }
    @Test(expected = NoSuchACompanyException.class)
    public void removeProductFromCompanyTest_NoSuchACompany() throws NoSuchAProductException {
        csclManager.removeProductFromCompany(12, adidas.getId());
    }
    @Test(expected = NoSuchAProductException.class)
    public void removeProductFromCompanyNoProExcTest()
            throws NoSuchAProductException {
        csclManager.createCompany(adidas);
        csclManager.removeProductFromCompany(13, adidas.getId());
    }
    @Test
    public void removeProductFromCompany1Test(@Mocked ProductManager productManager) throws NoSuchAProductException {
        new Expectations(){{
            productManager.findProductById(12);
            result = product;
        }};
        csclManager.createCompany(adidas);
        csclManager.addProductToCompany(12, adidas.getId());
        csclManager.removeProductFromCompany(product);
        Assert.assertEquals(0, adidas.getProductsIn().size());
    }
    @Test
    public void createCommentTest() throws UserNotAvailableException {
        csclManager.createComment(comment);
    }
    // TODO : write correct test for createScoreTest
    @Test
    public void createScoreTest(@Mocked ProductManager productManager, @Mocked CustomerManager customerManager) throws NoSuchAProductException, NotABuyer, NoSuchACustomerException {
        Score score = new Score("sapa", 1, 1);
        new MockUp<ProductManager>() {
            @Mock
            public void assignAScore(int productId, Score score1) {
                if (productId == 1) {
                    shoe.getAllScores().add(score);
                }
            }
        };
        new Expectations(){{
            customerManager.findCustomerById("sapa");
            result = sapa;
        }};
        csclManager.createScore("sapa", 1, 1);
        assertEquals(shoe.getAllScores().get(0).getProductId(), 1);
    }
    @Test(expected = NotABuyer.class)
    public void createScoreNotExistedProductExcTest() throws NoSuchAProductException, NotABuyer, NoSuchACustomerException {
        csclManager.createScore("sapa", 11, 2);
    }
    @Test
    public void getLogByIdTest() throws NoSuchALogException {
        SellLog actualSellLog = (SellLog) csclManager.getLogById(sellLog.getLogId());
        Assert.assertEquals(sellLog, actualSellLog);
    }
    @Test(expected = NoSuchALogException.class)
    public void getLogByIdNotFoundExcTest() throws NoSuchALogException {
        csclManager.getLogById(1000);
    }
    @Test
    public void createPurchaseLogTest() throws NoSuchSellerException {
        new MockUp<CustomerManager>() {
            @Mock
            public void addPurchaseLog(PurchaseLog log, Customer customer) {
                if (customer.getUsername().equals("sapa")) {
                    sapa.getPurchaseLogs().add(log);
                }
            }
        };
        csclManager.createPurchaseLog(cart, 10, sapa);
        assertEquals(sapa.getPurchaseLogs().size(), 2);
    }
    /*@Test
    public void createSellLogTest(@Mocked ProductManager productManager) throws NoSuchAProductException, NoSuchSellerException {
        new Expectations(){{
            DBManager.load(User.class, customer.getUsername());
            csclManager.findPrice(subCart);
            result = 12;
        }};
        csclManager.createSellLog(subCart, customer.getUsername(), 1);
        DBManager.save(subCart.getProduct());
        SoldProduct soldProduct = new SoldProduct();
        soldProduct.setSoldPrice(12);
        soldProduct.setSourceId(shoe.getId());
        SellLog sellLog = new SellLog(soldProduct, 12, 1,
                customer, new Date(), DeliveryStatus.DEPENDING);
        new Verifications(){{
            sajad.getSellLogs().add(sellLog);
        }};
        Assert.assertEquals(sajad.getSellLogs().get(0).getProduct().getSourceId(), 13);
    }*/
}
