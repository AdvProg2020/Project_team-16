package ModelPackage.System;

import ModelPackage.Product.*;
import ModelPackage.Users.Cart;
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
    private Category category;
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

        category = new Category("Cloth","CT321654987",null);

        HashMap<String,String> publicFeatures = new HashMap<>();
        publicFeatures.put("Color","Red");
        publicFeatures.put("Size","13*46*87");

        HashMap<String,String> specialFeatures = new HashMap<>();
        specialFeatures.put("size","XXL");
        specialFeatures.put("Material","Cotton");

        ArrayList<Score> scores = new ArrayList<>();
        scores.add(new Score("3215","PR20200405332158465",5));

        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("CM6541326548970",
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
        prices.put("asghar120",21000);
        prices.put("reza120",22000);
        prices.put("see120",21500);

        product = new Product("PR20200405332158465",
                ProductStatus.VERIFIED,"Shirt",new Date(),company,sellers,category,"CT321654987",
                publicFeatures,specialFeatures,"Bullshit",scores,5,comments,stock,prices,
                20,8
        );

        product2 = new Product("PR19800404879154555",
                ProductStatus.VERIFIED,"Shirt XXL",new Date(),company,new ArrayList<>(sellers),category,"CT321654987",
                new HashMap<>(publicFeatures),new HashMap<>(specialFeatures),
                "Bull",scores,3,new ArrayList<>(comments),new HashMap<>(stock),
                new HashMap<>(prices),
                460,2);

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
        productManager.addAmountOfStock(product,"reza120",8);
        int expected = 20;
        int actual = product.getStock().get("reza120");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addAmountOfStockNegativeNumTest(){
        productManager.addAmountOfStock(product,"reza120",-8);
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
}
