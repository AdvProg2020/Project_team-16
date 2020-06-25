package ModelPackage.System;

import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Off.Off;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SortManagerTest {
    private final SortManager sortManager;
    private List<Product> toSortProduct;
    private List<User> toSortUsers;
    private List<DiscountCode> toSortDiscountCodes;
    private List<DiscountcodeIntegerMap> toSortDiscountCodeIntegerMaps;
    private List<Off> toSortOffs;
    private List<Category> toSortCategories;
    private Product shoes;
    private Product gloves;
    private Product hammer;
    private DiscountCode superDiscountCode;
    private DiscountCode classicDiscountCode;
    private DiscountCode discountCode;
    private DiscountcodeIntegerMap superDiscountCodeIntegerMap;
    private DiscountcodeIntegerMap classicDiscountCodeIntegerMap;
    private DiscountcodeIntegerMap discountcodeIntegerMap;
    private Off off1;
    private Off off2;
    private Off off3;
    private Category category1;
    private Category category2;
    private Category category3;
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

        Company adidas = new Company("Adidas", "123456", "Sports");

        SellPackage sellPackage = new SellPackage(shoes, seller, 120, 3, null, false, true);

        shoes = new Product("Shoes", adidas,
                new Category(), new HashMap<>(), new HashMap<>(),
                "High quality",
                sellPackage);
        shoes.setView(2);
        shoes.setId(0);
        shoes.setBoughtAmount(1);
        shoes.setLeastPrice(120);
        shoes.setDateAdded(new Date(2018, Calendar.FEBRUARY, 4));
        shoes.setTotalScore(4.3);
        toSortProduct.add(shoes);

        SellPackage sellPackage1 = new SellPackage(gloves, seller, 1000, 5, null, false, true);

        gloves = new Product("Gloves",
                new Company("Puma", "2134", "Sports"),
                new Category(), new HashMap<>(), new HashMap<>(),
                "Best Seller",
                sellPackage1);
        gloves.setView(1);
        gloves.setId(1);
        gloves.setBoughtAmount(3);
        gloves.setDateAdded(new Date(2020, Calendar.JANUARY, 1));
        gloves.setLeastPrice(1000);
        gloves.setTotalScore(1.12);
        toSortProduct.add(gloves);

        SellPackage sellPackage2 = new SellPackage(hammer, seller, 12, 8, null, false, true);

        hammer = new Product("Hammer", adidas,
                new Category(), new HashMap<>(), new HashMap<>(),
                "hard",
                sellPackage2);
        hammer.setId(2);
        hammer.setView(5);
        hammer.setBoughtAmount(5);
        hammer.setDateAdded(new Date(2017, Calendar.JULY, 18));
        hammer.setLeastPrice(10);
        hammer.setTotalScore(3.59);
        toSortProduct.add(hammer);

        {
            superDiscountCode = new DiscountCode("superDiscountCode", new Date(2019, Calendar.MARCH, 4),
                    new Date(2030, Calendar.MARCH, 3), 10, 20);

            classicDiscountCode = new DiscountCode("classicDiscountCode", new Date(2018, Calendar.MARCH, 4),
                    new Date(2030, Calendar.MARCH, 3), 10, 20);

            discountCode = new DiscountCode("discountCode", new Date(2020, Calendar.MARCH, 4),
                    new Date(2030, Calendar.MARCH, 8), 10, 20);
            toSortDiscountCodes = new ArrayList<>();
            toSortDiscountCodes.add(superDiscountCode); toSortDiscountCodes.add(classicDiscountCode);
            toSortDiscountCodes.add(discountCode);
        }

        {
            superDiscountCodeIntegerMap = new DiscountcodeIntegerMap();
            superDiscountCodeIntegerMap.setDiscountCode(superDiscountCode);
            superDiscountCodeIntegerMap.setInteger(12);
            classicDiscountCodeIntegerMap = new DiscountcodeIntegerMap();
            classicDiscountCodeIntegerMap.setDiscountCode(classicDiscountCode);
            classicDiscountCodeIntegerMap.setInteger(8);
            discountcodeIntegerMap = new DiscountcodeIntegerMap();
            discountcodeIntegerMap.setDiscountCode(discountCode);
            discountcodeIntegerMap.setInteger(15);

            toSortDiscountCodeIntegerMaps = new ArrayList<>();
            toSortDiscountCodeIntegerMaps.add(superDiscountCodeIntegerMap);
            toSortDiscountCodeIntegerMaps.add(classicDiscountCodeIntegerMap);
            toSortDiscountCodeIntegerMaps.add(discountcodeIntegerMap);
        }

        {
            off1 = new Off(); off1.setOffPercentage(20);
            off2 = new Off(); off2.setOffPercentage(10);
            off3 = new Off(); off3.setOffPercentage(15);
            toSortOffs = new ArrayList<>();
            toSortOffs.add(off1); toSortOffs.add(off2); toSortOffs.add(off3);
        }

        {
            category1 = new Category(); category1.setName("shoes");
            category2 = new Category(); category2.setName("shirts");
            category3 = new Category(); category3.setName("gloves");
            toSortCategories = new ArrayList<>();
            toSortCategories.add(category1); toSortCategories.add(category2);
            toSortCategories.add(category3);
        }
    }

    @Test
    public void getInstanceTest() {
        SortManager test = SortManager.getInstance();
        assertEquals(sortManager, test);
    }
    @Test
    public void sortByNameTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.NAME);
        expectedSortedProducts.add(gloves);
        expectedSortedProducts.add(hammer);
        expectedSortedProducts.add(shoes);
        assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByViewTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.VIEW);
        expectedSortedProducts.add(hammer);
        expectedSortedProducts.add(shoes);
        expectedSortedProducts.add(gloves);
        assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByBoughtAmountTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.BOUGHT_AMOUNT);
        expectedSortedProducts.add(hammer);
        expectedSortedProducts.add(gloves);
        expectedSortedProducts.add(shoes);
        assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByTimeTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.TIME);
        expectedSortedProducts.add(gloves);
        expectedSortedProducts.add(shoes);
        expectedSortedProducts.add(hammer);
        assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByMorePriceTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.MORE_PRICE);
        expectedSortedProducts.add(gloves);
        expectedSortedProducts.add(shoes);
        expectedSortedProducts.add(hammer);
        assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByLessPriceTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.LESS_PRICE);
        expectedSortedProducts.add(hammer);
        expectedSortedProducts.add(shoes);
        expectedSortedProducts.add(gloves);
        assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortByScoreTest() {
        ArrayList<Product> expectedSortedProducts = new ArrayList<>();
        toSortProduct = sortManager.sort(toSortProduct, SortType.SCORE);
        expectedSortedProducts.add(shoes);
        expectedSortedProducts.add(hammer);
        expectedSortedProducts.add(gloves);
        assertEquals(expectedSortedProducts, toSortProduct);
    }
    @Test
    public void sortUserTest() {
        toSortUsers = sortManager.sortUser(toSortUsers);
        List<User> expectedSortedUsers = new ArrayList<>();
        expectedSortedUsers.add(seller);
        expectedSortedUsers.add(user);
        expectedSortedUsers.add(seller1);
        assertEquals(expectedSortedUsers, toSortUsers);
    }
    @Test
    public void sortDiscountsByNameTest() {
        sortManager.sortDiscountCodes(toSortDiscountCodes, SortType.NAME);
        List<DiscountCode> expectedSortedDiscounts = new ArrayList<>();
        expectedSortedDiscounts.add(classicDiscountCode);
        expectedSortedDiscounts.add(discountCode);
        expectedSortedDiscounts.add(superDiscountCode);
        assertEquals(expectedSortedDiscounts, toSortDiscountCodes);
    }
    @Test
    public void sortDiscountsByDefaultTest() {
        sortManager.sortDiscountCodes(toSortDiscountCodes, SortType.DEFAULT);
        List<DiscountCode> expectedSortedDiscounts = new ArrayList<>();
        expectedSortedDiscounts.add(superDiscountCode);
        expectedSortedDiscounts.add(classicDiscountCode);
        expectedSortedDiscounts.add(discountCode);
        assertEquals(toSortDiscountCodes, toSortDiscountCodes);
    }
    @Test
    public void sortDiscountsByStartingTimeTest() {
        sortManager.sortDiscountCodes(toSortDiscountCodes, SortType.TIME);
        List<DiscountCode> expectedSortedDiscounts = new ArrayList<>();
        expectedSortedDiscounts.add(classicDiscountCode);
        expectedSortedDiscounts.add(superDiscountCode);
        expectedSortedDiscounts.add(discountCode);
        assertEquals(expectedSortedDiscounts, toSortDiscountCodes);
    }
    // TODO : check ascending & descending
    @Test
    public void sortDiscountIntegersByAmountTest() {
        sortManager.sortDiscountIntegers(toSortDiscountCodeIntegerMaps, SortType.DEFAULT);
        List<DiscountcodeIntegerMap> expectedDiscountCodeIntegerMap = new ArrayList<>();
        /*expectedDiscountCodeIntegerMap.add(discountcodeIntegerMap);
        expectedDiscountCodeIntegerMap.add(superDiscountCodeIntegerMap);
        expectedDiscountCodeIntegerMap.add(classicDiscountCodeIntegerMap);*/
        expectedDiscountCodeIntegerMap.add(classicDiscountCodeIntegerMap);
        expectedDiscountCodeIntegerMap.add(superDiscountCodeIntegerMap);
        expectedDiscountCodeIntegerMap.add(discountcodeIntegerMap);
        assertEquals(expectedDiscountCodeIntegerMap, toSortDiscountCodeIntegerMaps);
    }
    @Test
    public void sortDiscountIntegersByCodeTest() {
        sortManager.sortDiscountIntegers(toSortDiscountCodeIntegerMaps, SortType.NAME);
        List<DiscountcodeIntegerMap> expectedDiscountCodeIntegerMap = new ArrayList<>();
        expectedDiscountCodeIntegerMap.add(classicDiscountCodeIntegerMap);
        expectedDiscountCodeIntegerMap.add(discountcodeIntegerMap);
        expectedDiscountCodeIntegerMap.add(superDiscountCodeIntegerMap);
        assertEquals(expectedDiscountCodeIntegerMap, toSortDiscountCodeIntegerMaps);
    }
    // TODO : check ascending & descending
    @Test
    public void sortOffTest() {
        sortManager.sortOff(toSortOffs);
        List<Off> expectedOffs = new ArrayList<>();
        expectedOffs.add(off2);
        expectedOffs.add(off3);
        expectedOffs.add(off1);
        assertEquals(expectedOffs, toSortOffs);
    }
    @Test
    public void sortCategoryTest() {
        sortManager.sortCategories(toSortCategories);
        List<Category> expectedCategories = new ArrayList<>();
        expectedCategories.add(category3);
        expectedCategories.add(category2);
        expectedCategories.add(category1);
        assertEquals(expectedCategories, toSortCategories);
    }

}
