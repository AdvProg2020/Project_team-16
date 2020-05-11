
package ModelPackage.System;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Request;
import ModelPackage.Users.Seller;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProductManagerTest {
    private ProductManager productManager;
    private Product product;
    private Product product2;
    private Seller asghar;
    private Seller reza;
    private Seller saeed;
    private Company company;

    {
        productManager = ProductManager.getInstance();
    }

    @Test
    public void createDatabase(){
        company = new Company("Adidas","115", "Clothing",new ArrayList<>());

        asghar = new Seller("asghar120","321","asghar","kkk",
                "asghar@gmail.com","897465",new Cart(),company,540);

        reza = new Seller("reza120","321","reza","kkk",
                "reza@gmail.com","897465",new Cart(),company,550);

        saeed = new Seller("see120","321","see","kkk",
                "see@gmail.com","897465",new Cart(),company,540);
        DBManager.save(company);
        DBManager.save(saeed);
        DBManager.save(reza);
        DBManager.save(asghar);

        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(asghar);
        sellers.add(reza);
        sellers.add(saeed);

        Category category = new Category("Cloth", null);
        DBManager.save(category);

        HashMap<String,String> publicFeatures = new HashMap<>();
        publicFeatures.put("Color","Red");
        publicFeatures.put("Size","13*46*87");

        HashMap<String,String> specialFeatures = new HashMap<>();
        specialFeatures.put("size","XXL");
        specialFeatures.put("Material","Cotton");

        ArrayList<Score> scores = new ArrayList<>();
        scores.add(new Score("3215",1,5));
        DBManager.save(scores.get(0));

        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("reza120",
                "good",
                "it was good",
                CommentStatus.VERIFIED,
                true));
        DBManager.save(comments.get(0));

        ArrayList<SellerIntegerMap> stock = new ArrayList<>();
        SellerIntegerMap asgharStock = new SellerIntegerMap(asghar,20);
        SellerIntegerMap rezaStock = new SellerIntegerMap(reza,12);
        SellerIntegerMap saeedStock = new SellerIntegerMap(saeed,25);
        stock.add(asgharStock);
        stock.add(rezaStock);
        stock.add(saeedStock);

        ArrayList<SellerIntegerMap> prices = new ArrayList<>();
        SellerIntegerMap asgharPrice = new SellerIntegerMap(asghar,25000);
        SellerIntegerMap rezaPrice = new SellerIntegerMap(reza,22000);
        SellerIntegerMap saeedPrice = new SellerIntegerMap(saeed,21500);
        prices.add(asgharPrice);
        prices.add(rezaPrice);
        prices.add(saeedPrice);

        for (SellerIntegerMap map : stock) {
            DBManager.save(map);
        }

        for (SellerIntegerMap map : prices) {
            DBManager.save(map);
        }

        product = new Product("Shirt","Adidas",sellers,category.getCategoryId(),publicFeatures,specialFeatures,
                "bulshit",stock,prices);
        product.setAllComments(comments);
        product.setAllScores(scores);
        product.setTotalScore(5);
        product.setView(20);
        product.setBoughtAmount(8);
        product.setProductStatus(ProductStatus.VERIFIED);

        DBManager.save(product);

        product2 = new Product("Shirt XXL","Adidas",new ArrayList<>(sellers),category.getCategoryId(),new HashMap<>(publicFeatures),new HashMap<>(specialFeatures),
                "Bull",new ArrayList<>(),new ArrayList<>());
        product2.setTotalScore(3);
        product2.setView(460);
        product2.setProductStatus(ProductStatus.UNDER_EDIT);

        DBManager.save(product2);
    }

    @Test
    public void getInstanceTest(){
        ProductManager test = ProductManager.getInstance();
        Assert.assertEquals(test,productManager);
    }

    @Test
    public void addAmountOfStockTest(){
        productManager.addAmountOfStock(14,"reza120",1);
        int expected = 13;
        product = DBManager.load(Product.class,14);
        int actual = product.getStock().get(1).getInteger();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addAmountOfStockNegativeNumTest(){
        productManager.addAmountOfStock(14,"reza120",-8);
        int expected = 5;
        product = DBManager.load(Product.class,14);
        int actual = product.getStock().get(1).getInteger();
        Assert.assertEquals(expected,actual);
    }

    /*@Test
    public void FindProductByIdTest(){
        Product found = productManager.findProductById("PR20200405332158465");
        Assert.assertEquals(product,found);
    }

    @Test
    public void FindProductByIdNotFoundTest(){
        Product found = productManager.findProductById("PR20200425332158465");
        Assert.assertNull(found);
    }

    @Test
    public void FindProductByName(){
        Product[] expected = new Product[2];
        expected[0] = product;
        expected[1] = product2;

        Product[] actual = productManager.findProductByName("ShiRt");
        Assert.assertArrayEquals(expected,actual);
    }

    @Test
    public void addViewTest(){
        productManager.addView(product.getId());
        int expected = 21;
        int actual = product.getView();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addBoughtTest(){
        productManager.addBought(product.getId());
        int expected = 9;
        int actual = product.getBoughtAmount();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void assignACommentTest(){
        Comment comment = new Comment("PR20200405332158465",
                "reza120",
                "very Good",
                "Awesome !",
                CommentStatus.VERIFIED,
                true);
        productManager.assignAComment(product.getId(),comment);
        Assert.assertEquals(comment.getId(),product.getAllComments().get(1).getId());
    }

    @Test
    public void assignAScoreTest(){
        Score score = new Score("asghar120","PR20200405332158465",4);
        productManager.assignAScore("PR20200405332158465",score);
        double expected = 4.5;
        double actual = product.getTotalScore();

        Assert.assertEquals(expected,actual,0);
    }

    @Test
    public void showCommentsTest(){
        Comment[] comments = productManager.showComments(product.getId());
        Assert.assertEquals(product.getAllComments().get(0),comments[0]);
    }

    @Test
    public void showScoresTest(){
        Score[] scores = productManager.showScores(product.getId());
        Assert.assertEquals(product.getAllScores().get(0),scores[0]);
    }

    @Test
    public void doesThisProductExistTest(){
        boolean actual = productManager.doesThisProductExist("PR20200405332158465");
        Assert.assertTrue(actual);
        actual = productManager.doesThisProductExist("PR20200405852158465");
        Assert.assertFalse(actual);
    }

    @Test
    public void isThisProductAvailableTest(){
        boolean actual = productManager.isThisProductAvailable(product2.getId());
        Assert.assertFalse(actual);

        actual = productManager.isThisProductAvailable(product.getId());
        Assert.assertTrue(actual);
    }

    @Test
    public void leastPriceOfTest(){
        int actual = productManager.leastPriceOf("PR20200405332158465");
        int expected = 21500;
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void deleteProductTest() throws Exception{
        productManager.deleteProduct(product.getId());
        boolean exist = productManager.doesThisProductExist(product.getId());
        Assert.assertFalse(exist);
    }

    @Test
    public void createProductTest(){
        productManager.clear();
        productManager.createProduct(product,"asghar120");
        Request request = RequestManager.getInstance().getRequests().get(0);
        RequestManager.getInstance().accept(request.getRequestId());
        boolean successful = productManager.doesThisProductExist(product.getId());
        Assert.assertTrue(successful);
    }

    @Test
    public void editProductTest(){
        productManager.clear();
        productManager.addProductToList(product);
        Product edit = new Product(product);
        edit.setCompany("Brazzers");
        productManager.editProduct(edit,"asghar120");
        Request request = RequestManager.getInstance().getRequests().get(0);
        RequestManager.getInstance().accept(request.getRequestId());
        String company = productManager.findProductById(product.getId()).getCompany();

        Assert.assertEquals("Brazzers",company);
    }

    @Test
    public void allFeaturesOfTest(){
        HashMap<String,String> features = new HashMap<>();
        features.put("Color","Red");
        features.put("Size","13*46*87");
        features.put("size","XXL");
        features.put("Material","Cotton");

        Assert.assertEquals(features,productManager.allFeaturesOf(product));
    }*/
}