package ModelPackage.System;

import ModelPackage.Product.Product;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SortManagerTest {
    private SortManager sortManager;
    private ArrayList<Product> list;
    private ArrayList<Product> toSortProduct;
    private Product product;
    private Product product1;
    private Product product2;
    {
        sortManager = SortManager.getInstance();
        toSortProduct = new ArrayList<>();
        list = new ArrayList<>();
        product = new Product("Shoes", "Adidas", new ArrayList<>(),
                "12", new HashMap<>(), new HashMap<>(), "High quality",
                new HashMap<>(), new HashMap<>());
        product.setView(2);
        product.setProductId("0");
        product.setBoughtAmount(1);
        product.setDateAdded(new Date(2018, Calendar.FEBRUARY, 4));
        toSortProduct.add(product);
        product1 = new Product("Gloves", "Puma", new ArrayList<>(),
                "13", new HashMap<>(), new HashMap<>(), "Best Seller",
                new HashMap<>(), new HashMap<>());
        product1.setView(1);
        product1.setProductId("1");
        product1.setBoughtAmount(3);
        product1.setDateAdded(new Date(2020, Calendar.JANUARY, 1));
        toSortProduct.add(product1);
        product2 = new Product("Hammer", "Agriculture Company", new ArrayList<>(),
                "20", new HashMap<>(), new HashMap<>(), "hard",
                new HashMap<>(), new HashMap<>());
        product2.setProductId("2");
        product2.setView(5);
        product2.setBoughtAmount(5);
        product2.setDateAdded(new Date(2017, Calendar.JULY, 18));
        toSortProduct.add(product2);
    }
    @Test
    public void getInstanceTest() {
        SortManager test = SortManager.getInstance();
        Assert.assertEquals(sortManager, test);
    }
    @Test
    public void sortByNameTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        list = sortManager.sort(toSortProduct, SortType.NAME);
        expectedSortedProducts.add(product1);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product);
        Assert.assertEquals(expectedSortedProducts, list);
    }
    @Test
    public void sortByViewTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        list = sortManager.sort(toSortProduct, SortType.VIEW);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product);
        expectedSortedProducts.add(product1);
        Assert.assertEquals(expectedSortedProducts, list);
    }
    @Test
    public void sortByBoughtAmountTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        list = sortManager.sort(toSortProduct, SortType.BOUGHTAMOUNT);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product1);
        expectedSortedProducts.add(product);
        Assert.assertEquals(expectedSortedProducts, list);
    }
    @Test
    public void sortByTimeTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        list = sortManager.sort(toSortProduct, SortType.TIME);
        expectedSortedProducts.add(product1);
        expectedSortedProducts.add(product);
        expectedSortedProducts.add(product2);
        Assert.assertEquals(expectedSortedProducts, list);
    }
}
