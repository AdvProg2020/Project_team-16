
package ModelPackage.System;

import ModelPackage.Product.*;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Request;
import ModelPackage.Users.Seller;
import org.junit.Assert;
import org.junit.Test;

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

    //Creating a Product
    {
        productManager = ProductManager.getInstance();
        company = new Company("Adidas","115", "Clothing",new ArrayList<>());

        asghar = new Seller("asghar120","321","asghar","kkk",
                            "asghar@gmail.com","897465",new Cart(),company,540);

        reza = new Seller("reza120","321","reza","kkk",
                "reza@gmail.com","897465",new Cart(),company,550);

        saeed = new Seller("see120","321","see","kkk",
            "see@gmail.com","897465",new Cart(),company,540);

        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(asghar);
        sellers.add(reza);
        sellers.add(saeed);

        Category category = new Category("Cloth", null);

        HashMap<String,String> publicFeatures = new HashMap<>();
        publicFeatures.put("Color","Red");
        publicFeatures.put("Size","13*46*87");

        HashMap<String,String> specialFeatures = new HashMap<>();
        specialFeatures.put("size","XXL");
        specialFeatures.put("Material","Cotton");

        ArrayList<Score> scores = new ArrayList<>();
        scores.add(new Score("3215","PR20200405332158465",5));

        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("PR20200405332158465",
                "reza120",
                "good",
                "it was good",
                CommentStatus.VERIFIED,
                true));

        HashMap<String,Integer> stock = new HashMap<>();
        stock.put("asghar120",20);
        stock.put("reza120",12);
        stock.put("see120",25);

        HashMap<String,Integer> prices = new HashMap<>();
        prices.put("asghar120",25000);
        prices.put("reza120",22000);
        prices.put("see120",21500);

        product = new Product("Shirt","Adidas",sellers,category.getId(),publicFeatures,specialFeatures,
                "bulshit",stock,prices);
        product.setAllComments(comments);
        product.setAllScores(scores);
        product.setTotalScore(5);
        product.setView(20);
        product.setBoughtAmount(8);
        product.setProductId("PR20200405332158465");
        product.setProductStatus(ProductStatus.VERIFIED);


        product2 = new Product("Shirt XXL","Adidas",new ArrayList<>(sellers),category.getId(),new HashMap<>(publicFeatures),new HashMap<>(specialFeatures),
                "Bull",new HashMap<>(stock),new HashMap<>(prices));
        product2.setAllComments(comments);
        product2.setAllScores(scores);
        product2.setTotalScore(3);
        product2.setView(460);
        product2.setBoughtAmount(2);
        product2.setProductId("PR20200407545358465");
        product2.setProductStatus(ProductStatus.UNDER_EDIT);

        productManager.clear();
        productManager.addProductToList(product);
        productManager.addProductToList(product2);
    }

    @Test
    public void getInstanceTest(){
        ProductManager test = ProductManager.getInstance();
        Assert.assertEquals(test,productManager);
    }

    @Test
    public void addAmountOfStockTest(){
        productManager.addAmountOfStock(product.getProductId(),"reza120",8);
        int expected = 20;
        int actual = product.getStock().get("reza120");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addAmountOfStockNegativeNumTest(){
        productManager.addAmountOfStock(product.getProductId(),"reza120",-8);
        int expected = 4;
        int actual = product.getStock().get("reza120");
        Assert.assertEquals(expected,actual);
    }

    @Test
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
        productManager.addView(product.getProductId());
        int expected = 21;
        int actual = product.getView();

        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addBoughtTest(){
        productManager.addBought(product.getProductId());
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
        productManager.assignAComment(product.getProductId(),comment);
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
        Comment[] comments = productManager.showComments(product.getProductId());
        Assert.assertEquals(product.getAllComments().get(0),comments[0]);
    }

    @Test
    public void showScoresTest(){
        Score[] scores = productManager.showScores(product.getProductId());
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
        boolean actual = productManager.isThisProductAvailable(product2.getProductId());
        Assert.assertFalse(actual);

        actual = productManager.isThisProductAvailable(product.getProductId());
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
        productManager.deleteProduct(product.getProductId());
        boolean exist = productManager.doesThisProductExist(product.getProductId());
        Assert.assertFalse(exist);
    }

    @Test
    public void createProductTest(){
        productManager.clear();
        productManager.createProduct(product,"asghar120");
        Request request = RequestManager.getInstance().getRequests().get(0);
        RequestManager.getInstance().accept(request.getRequestId());
        boolean successful = productManager.doesThisProductExist(product.getProductId());
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
        String company = productManager.findProductById(product.getProductId()).getCompany();

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
    }
}