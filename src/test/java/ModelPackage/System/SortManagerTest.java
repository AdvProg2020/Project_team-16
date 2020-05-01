package ModelPackage.System;

import ModelPackage.Product.Product;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
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
        toSortProduct.add(product);
        product1 = new Product("Gloves", "Puma", new ArrayList<>(),
                "13", new HashMap<>(), new HashMap<>(), "Best Seller",
                new HashMap<>(), new HashMap<>());
        toSortProduct.add(product1);
        product2 = new Product("Hammer", "Agriculture Company", new ArrayList<>(),
                "20", new HashMap<>(), new HashMap<>(), "hard",
                new HashMap<>(), new HashMap<>());
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
}
