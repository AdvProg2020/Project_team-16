package ModelPackage.System;

import ModelPackage.Product.*;
import ModelPackage.Users.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class CSCLManagerTest {
    private CSCLManager csclManager;
    private Company adidas;
    private Company puma;
    private String[] data = {"12", "ali12", "solution", "solved correctly"};
    private Product product;
    private Cart cart;
    private SubCart subCart;
    private Comment comment;
    private Comment comment1;
    {
        csclManager = CSCLManager.getInstance();
        adidas = new Company("Adidas", "34524532", "Sports");
        puma = new Company("Puma", "12434565", "Sports");
        product = new Product();
        product.setProductId("12");
        subCart = new SubCart();
        cart = new Cart();
        cart.getSubCarts().add(subCart);
        ProductManager.getInstance().addProductToList(product);
        csclManager.createCompany(adidas);
        csclManager.createCompany(puma);
        comment = new Comment("PR20200405332158465",
                "reza120",
                "very Good",
                "Awesome !",
                CommentStatus.VERIFIED,
                true);
        comment1 = new Comment("12", "ali12", "very Good", "Awesome!", CommentStatus.VERIFIED, false);
        csclManager.getAllComments().add(comment1);
    }
    @Test
    public void getInstanceTest() {
        CSCLManager test = CSCLManager.getInstance();
        Assert.assertEquals(test, csclManager);
    }
    @Test
    public void getCompanyByNameTest() {
        String name = "Adidas";
        Company actualCompany = csclManager.getCompanyByName(name);
        Company expectedCompany = adidas;
        Assert.assertEquals(actualCompany, expectedCompany);
    }
    @Test
    public void  getCompanyByNameNotFoundTest() {
        String name = "Pish";
        Company actualCompany = csclManager.getCompanyByName(name);
        Assert.assertNull(actualCompany);
    }
    @Test
    public void editCompanyNameTest() {
        String expectedName = "Pishgaman";
        csclManager.editCompanyName("Adidas", expectedName);
        String actualName = adidas.getName();
        Assert.assertEquals(expectedName, actualName);
    }
    @Test
    public void editCompanyGroupTest() {
        String expectedGroup = "SuperSport";
        csclManager.editCompanyGroup("Puma", expectedGroup);
        String actualGroup = puma.getGroup();
        Assert.assertEquals(expectedGroup, actualGroup);
    }
    @Test
    public void doesCompanyExistTest() {
        boolean actual = csclManager.doesCompanyExist("Adidas");
        Assert.assertTrue(actual);
        actual = csclManager.doesCompanyExist("pishgaman");
        Assert.assertFalse(actual);
    }
    @Test
    public void addProductToCompanyTest() {
        csclManager.addProductToCompany("12", "Adidas");
        String actualId = adidas.getProductsIn().get(0).getProductId();
        Assert.assertEquals("12", actualId);
    }
    @Test
    public void removeProductFromCompanyTest() {
        csclManager.addProductToCompany("12", "Puma");
        csclManager.removeProductFromCompany("12", "Puma");
        int actualSize = puma.getProductsIn().size();
        Assert.assertEquals(0, actualSize);
    }
    @Test
    public void createScoreTest() {
        csclManager.createScore("Akbar", "12", 4);
        Score score = new Score("Akbar", "12", 4);
        int a = Comparator.comparing(Score::getUserId).thenComparing(Score::getProductId).
                thenComparing(Score::getScore).compare(
                        csclManager.getAllScores().get(0), score);
        Assert.assertEquals(0, a);
    }
    @Test
    public void createPurchaseLogTest() {
        csclManager.createPurchaseLog(new Cart(), 2);
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
    public void createCommentTest() {
        csclManager.createComment(comment);
        Request request = RequestManager.getInstance().getRequests().get(0);
        RequestManager.getInstance().accept(request.getRequestId());
        boolean successful = csclManager.doesThisCommentExists(comment.getId());
        Assert.assertTrue(successful);
    }
    @Test
    public void doesThisCommentExistTest() {
        comment1.setId("PRCOM1");
        boolean actual = csclManager.doesThisCommentExists("PRCOM1");
        Assert.assertTrue(actual);
        actual = csclManager.doesThisCommentExists("PRCOM2");
        Assert.assertFalse(actual);
    }
}
