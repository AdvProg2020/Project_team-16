package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.Log;
import ModelPackage.Log.SellLog;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.clcsmanager.NoSuchACompanyException;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import ModelPackage.Users.*;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CSCLManagerTest {
    private CSCLManager csclManager;
    private Company adidas;
    private Company puma;
    private Company nike;
    private Product product;
    private Product shoe;
    private Cart cart;
    private SubCart subCart;
    private Comment comment;
    private SellLog sellLog;
    private Customer customer;
    private Seller sajad;
    {
        csclManager = CSCLManager.getInstance();
        adidas = new Company("Adidas", "34524532", "Sports");
        puma = new Company("Puma", "12434565", "Sports");
        nike = new Company("Nike", "2123435", "Sports");
        csclManager.createCompany(puma);
        csclManager.createCompany(nike);
        shoe = new Product("Shoes", "Adidas", new ArrayList<>(), new Category(),
                new HashMap<>(), new HashMap<>(), "good", new ArrayList<>(), new ArrayList<>());
        shoe.setId(13);
        product = new Product();
        product.setId(12);
        sajad = new Seller("sapa", "1234", "sajad", "paksima", "sajad@gmail.com",
                "12345", new Cart(), adidas, 12);
        subCart = new SubCart(shoe, sajad, 1);
        cart = new Cart();
        cart.getSubCarts().add(subCart);
        CategoryManager.getInstance().addProductToCategory(product, new Category());
        comment = new Comment("reza120",
                "very Good",
                "Awesome !",
                CommentStatus.VERIFIED,
                true);
        comment.setProduct(product);

        customer = new Customer("ali110", "12434", "Ali", "Alavi",
                "alavi@gmail.com", "123435", new Cart(), 21);

        sellLog = new SellLog();
        DBManager.save(sellLog);
    }
    @Test
    public void getInstanceTest() {
        CSCLManager test = CSCLManager.getInstance();
        Assert.assertEquals(test, csclManager);
    }
    @Test
    public void createCompanyTest() {
        csclManager.createCompany(adidas);
        new Verifications(){{
            DBManager.save(adidas);
        }};
        Assert.assertEquals(adidas, DBManager.load(Company.class, adidas.getId()));
    }
    @Test
    public void getCompanyByIdTest() {
        new Expectations(){{
            DBManager.load(Company.class, puma.getId());
        }};
        Company actual = csclManager.getCompanyById(puma.getId());
        Assert.assertEquals(puma, actual);
    }
    @Test(expected = NoSuchACompanyException.class)
    public void getCompanyByIdNotFountExcTest() {
        csclManager.getCompanyById(adidas.getId());
    }
    @Test
    public void editCompanyNameTest() {
        new Expectations(){{
            DBManager.load(Company.class, puma.getId());
        }};
        csclManager.editCompanyName(puma.getId(), "adidas");
        new Verifications(){{
            DBManager.save(puma);
        }};
        Assert.assertEquals("adidas", puma.getName());
    }
    @Test(expected = NoSuchACompanyException.class)
    public void editCompanyNameNotFoundExcTest() {
        csclManager.editCompanyName(adidas.getId(), "Adidas");
    }
    @Test
    public void editCompanyGroupTest() {
        csclManager.editCompanyGroup(nike.getId(), "Sporty");
        new Verifications(){{
            DBManager.save(nike);
        }};
        Assert.assertEquals("Sporty", nike.getGroup());
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
        csclManager.addProductToCompany(12, nike.getId());
        new Verifications(){{
           DBManager.save(product);
           DBManager.save(nike);
        }};
        Assert.assertEquals(nike.getProductsIn().get(nike.getProductsIn().size() - 1), product);
    }
    @Test(expected = NoSuchACompanyException.class)
    public void addProductToCartNotFoundComExcTest()
            throws NoSuchAProductException {
        csclManager.addProductToCompany(12, adidas.getId());
    }
    @Test(expected = NoSuchAProductException.class)
    public void addProductToCartNotFoundProExcTest()
            throws NoSuchAProductException {
        csclManager.addProductToCompany(13, puma.getId());
    }
    @Test
    public void removeProductFromCompanyTest(@Mocked ProductManager productManager)
            throws NoSuchAProductException {
        new Expectations(){{
            productManager.findProductById(12);
            result = product;
        }};
        csclManager.addProductToCompany(12, puma.getId());
        new Verifications(){{
            DBManager.save(product);
            DBManager.save(puma);
        }};
        int oldSize = puma.getProductsIn().size();
        csclManager.removeProductFromCompany(12, puma.getId());
        new Verifications(){{
            DBManager.save(puma);
            DBManager.save(product);
        }};
        int newSize = puma.getProductsIn().size();
        Assert.assertEquals(newSize + 1, oldSize);
    }
    @Test(expected = NoSuchACompanyException.class)
    public void removeProductFromCompanyTest() throws NoSuchAProductException {
        csclManager.removeProductFromCompany(12, adidas.getId());
    }
    @Test(expected = NoSuchAProductException.class)
    public void removeProductFromCompanyNoProExcTest()
            throws NoSuchAProductException {
        csclManager.removeProductFromCompany(13, puma.getId());
    }
    /*@Test
    public void removeProductFromCompany1Test() {
        csclManager.removeProductFromCompany(shoe);
        new Verifications(){{
            DBManager.save(shoe);
            DBManager.save(puma);
        }};
        Assert.assertEquals(0, puma.getProductsIn().size());
    }*/
    @Test
    public void createCommentTest(@Mocked RequestManager requestManager) throws NoSuchARequestException {
        new Expectations(){{
            DBManager.save(comment);
        }};
        csclManager.createComment(comment);
        Request request = new Request("reza120", RequestType.ASSIGN_COMMENT,
                "User (reza120) has requested to assign a comment on product (12) :\n" +
                        "Title : very Good\n" +
                        "Text : Awesome !", comment);
        Assert.assertNotNull(requestManager.findRequestById(request.getRequestId()));
    }
    // TODO : write correct test for createScoreTest
    @Test
    public void createScoreTest(@Mocked ProductManager productManager) throws NoSuchAProductException {
        new Expectations(){{
            productManager.findProductById(13);
            result = shoe;
        }};
        Score score = new Score("ali110", 13, 1);
        csclManager.createScore("ali110", 13, 1);
        productManager.assignAScore(13, score);
        Assert.assertEquals(shoe.getAllScores().get(0).getProductId(), 13);
    }
    @Test(expected = NoSuchAProductException.class)
    public void createScoreNotExistedProductExcTest() throws NoSuchAProductException {
        csclManager.createScore("ali110", 13, 2);
    }
    @Test
    public void createSellLogTest(@Mocked ProductManager productManager) throws NoSuchAProductException {
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
    }
    @Test
    public void getLogByIdTest() {
        new Expectations(){{
            DBManager.load(Log.class, sellLog.getLogId());
        }};
        SellLog actualSellLog = (SellLog) csclManager.getLogById(sellLog.getLogId());
        Assert.assertEquals(sellLog, actualSellLog);
    }
    @Test(expected = NoSuchALogException.class)
    public void getLogByIdNotFoundExcTest() {
        csclManager.getLogById(10000);
    }
    @Test
    public void allBuyersTest() {
        
    }
}
