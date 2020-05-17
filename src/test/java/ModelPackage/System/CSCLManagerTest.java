package ModelPackage.System;

import ModelPackage.Product.*;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import ModelPackage.Users.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class CSCLManagerTest {
    private CSCLManager csclManager;
    private Company adidas;
    private Company puma;
    private Company nike;
    private Product product;
    private Cart cart;
    private SubCart subCart;
    private Comment comment;
    private Comment comment1;
    private Customer customer;
    {
        csclManager = CSCLManager.getInstance();
        adidas = new Company("Adidas", "34524532", "Sports");
        puma = new Company("Puma", "12434565", "Sports");
        nike = new Company("Nike", "2123435", "Sports");
        product = new Product();
        product.setId(12);
        subCart = new SubCart();
        cart = new Cart();
        cart.getSubCarts().add(subCart);
        CategoryManager.getInstance().addProductToCategory(product, new Category());
        csclManager.createCompany(adidas);
        csclManager.createCompany(puma);
        comment = new Comment("reza120",
                "very Good",
                "Awesome !",
                CommentStatus.VERIFIED,
                true);
        comment1 = new Comment("ali12", "very Good",
                "Awesome!", CommentStatus.VERIFIED, false);
        //csclManager.getAllComments().add(comment1);
        csclManager.createComment(comment1);

        customer = new Customer("ali110", "12434", "Ali", "Alavi",
                "alavi@gmail.com", "123435", new Cart(), 21);
    }
    @Test
    public void getInstanceTest() {
        CSCLManager test = CSCLManager.getInstance();
        Assert.assertEquals(test, csclManager);
    }
    @Test
    public void getCompanyByNameTest() {
        Company actualCompany = csclManager.getCompanyById(adidas.getId());
        Company expectedCompany = adidas;
        Assert.assertEquals(actualCompany, expectedCompany);
    }
    @Test
    public void  getCompanyByNameNotFoundTest() {
        Company actualCompany = csclManager.getCompanyById(nike.getId());
        Assert.assertNull(actualCompany);
    }
    @Test
    public void editCompanyNameTest() {
        String expectedName = "Pishgaman";
        csclManager.editCompanyName(adidas.getId(), expectedName);
        String actualName = adidas.getName();
        Assert.assertEquals(expectedName, actualName);
    }
    @Test
    public void editCompanyGroupTest() {
        String expectedGroup = "SuperSport";
        csclManager.editCompanyGroup(puma.getId(), expectedGroup);
        String actualGroup = puma.getGroup();
        Assert.assertEquals(expectedGroup, actualGroup);
    }
    @Test
    public void addProductToCompanyTest() throws NoSuchAProductException {
        csclManager.addProductToCompany(12, adidas.getId());
        int actualId = adidas.getProductsIn().get(0).getId();
        Assert.assertEquals(12, actualId);
    }
    @Test
    public void removeProductFromCompanyTest() throws NoSuchAProductException {
        csclManager.addProductToCompany(12, puma.getId());
        csclManager.removeProductFromCompany(12, puma.getId());
        int actualSize = puma.getProductsIn().size();
        Assert.assertEquals(0, actualSize);
    }
    @Test
    public void createScoreTest() throws NoSuchAProductException {
        csclManager.createScore("Akbar", 12, 4);
        Score score = new Score("Akbar", 12, 4);
        int a = Comparator.comparing(Score::getUserId).thenComparing(Score::getProductId).
                thenComparing(Score::getScore).compare(
                        ProductManager.getInstance().showScores(12)[0], score);
        Assert.assertEquals(0, a);
    }
    @Test
    public void createPurchaseLogTest() {
        csclManager.createPurchaseLog(customer.getCart(), 2, customer);
        csclManager.getAllLogs().get(0).setLogId("12");
        String actualId = csclManager.getAllLogs().get(0).getLogId();
        Assert.assertEquals("12", actualId);
    }
    @Test
    public void createSellLogTest() {
        csclManager.createSellLog(subCart, "Ali12", 12);
        csclManager.getAllLogs().get(0).setLogId("1");
        String actualId = csclManager.getAllLogs().get(0).getLogId();
        Assert.assertEquals("1", actualId);
    }
    @Test
    public void createCommentTest() throws NoSuchARequestException, NoSuchAProductException {
        csclManager.createComment(comment);
        Request request = RequestManager.getInstance().getRequests().get(0);
        RequestManager.getInstance().accept(request.getRequestId());
        boolean successful = csclManager.doesThisCommentExists(comment.getId());
        Assert.assertTrue(successful);
    }
    @Test
    public void doesThisCommentExistTest() {
        boolean actual = csclManager.doesThisCommentExists(comment1.getId());
        Assert.assertTrue(actual);
        actual = csclManager.doesThisCommentExists(comment.getId());
        Assert.assertFalse(actual);
    }
}
