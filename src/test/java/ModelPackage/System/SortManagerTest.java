package ModelPackage.System;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SortManagerTest {
    private SortManager sortManager;
    private List<Product> toSortProduct;
    private List<User> toSortUsers;
    private Product product;
    private Product product1;
    private Product product2;
    private Seller seller;
    private Seller seller1;
    private User user;
    {
        sortManager = SortManager.getInstance();
        toSortProduct = new ArrayList<>();
        toSortUsers = new ArrayList<>();
        seller = new Seller("Ali0018", "1243", "Ali",
                "Alavi", "ali@gmail.com", "0913223434", new Cart(),
                new Company("Adidas", "021123", "Sports"), 4);
        seller1 = new Seller("Saman0018", "1223443", "Saman",
                "Samani", "saman@gmail.com", "0913223435", new Cart(),
                new Company("Puma", "021123", "Sports"), 2);
        user = new User("Pasha1@", "1243", "Pasha",
                "Pashaei", "Pasha@gmail.com", "0913323484", new Cart());
        toSortUsers.add(seller);
        toSortUsers.add(seller1);
        toSortUsers.add(user);


        List<SellerIntegerMap> prices = new ArrayList<>();
        prices.add(new SellerIntegerMap(seller, 100000));
        prices.add(new SellerIntegerMap(seller1, 120));
        product = new Product("Shoes", "Adidas", new ArrayList<>(),
                new Category(), new HashMap<>(), new HashMap<>(), "High quality",
                new ArrayList<>(), prices);
        product.setView(2);
        product.setId(0);
        product.setBoughtAmount(1);
        product.setLeastPrice(120);
        product.setDateAdded(new Date(2018, Calendar.FEBRUARY, 4));
        product.setTotalScore(4.3);
        toSortProduct.add(product);

        List<SellerIntegerMap> prices1 = new ArrayList<>();
        prices1.add(new SellerIntegerMap(seller, 13000));
        prices1.add(new SellerIntegerMap(seller1, 1000));
        product1 = new Product("Gloves", "Puma", new ArrayList<>(),
                new Category(), new HashMap<>(), new HashMap<>(), "Best Seller",
                new ArrayList<>(), prices1);
        product1.setView(1);
        product1.setId(1);
        product1.setBoughtAmount(3);
        product1.setDateAdded(new Date(2020, Calendar.JANUARY, 1));
        product1.setLeastPrice(1000);
        product1.setTotalScore(1.12);
        toSortProduct.add(product1);

        List<SellerIntegerMap> prices2 = new ArrayList<>();
        prices2.add(new SellerIntegerMap(seller, 10));
        prices2.add(new SellerIntegerMap(seller1, 12));
        product2 = new Product("Hammer", "Agriculture Company", new ArrayList<>(),
                new Category(), new HashMap<>(), new HashMap<>(), "hard",
                new ArrayList<>(), prices2);
        product2.setId(2);
        product2.setView(5);
        product2.setBoughtAmount(5);
        product2.setDateAdded(new Date(2017, Calendar.JULY, 18));
        product2.setLeastPrice(10);
        product2.setTotalScore(3.59);
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
        toSortProduct = sortManager.sort(toSortProduct, SortType.NAME);
        expectedSortedProducts.add(product1);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product);
        Assert.assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByViewTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.VIEW);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product);
        expectedSortedProducts.add(product1);
        Assert.assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByBoughtAmountTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.BOUGHT_AMOUNT);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product1);
        expectedSortedProducts.add(product);
        Assert.assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByTimeTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.TIME);
        expectedSortedProducts.add(product1);
        expectedSortedProducts.add(product);
        expectedSortedProducts.add(product2);
        Assert.assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByMorePriceTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.MORE_PRICE);
        expectedSortedProducts.add(product1);
        expectedSortedProducts.add(product);
        expectedSortedProducts.add(product2);
        Assert.assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByLessPriceTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.LESS_PRICE);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product);
        expectedSortedProducts.add(product1);
        Assert.assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByScoreTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.SCORE);
        expectedSortedProducts.add(product);
        expectedSortedProducts.add(product2);
        expectedSortedProducts.add(product1);
        Assert.assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortUserTest() {
        toSortUsers = sortManager.sortUser(toSortUsers);
        List<User> expectedSortedUsers = new ArrayList<>();
        expectedSortedUsers.add(seller);
        expectedSortedUsers.add(user);
        expectedSortedUsers.add(seller1);
        Assert.assertEquals(expectedSortedUsers, toSortUsers);
    }
}
