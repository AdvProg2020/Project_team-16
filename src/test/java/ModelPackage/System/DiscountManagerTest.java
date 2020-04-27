package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Users.Cart;
import ModelPackage.Users.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DiscountManagerTest {
    private DiscountManager discountManager;
    private DiscountCode discountCode;
    private DiscountCode discountCode1;
    private DiscountCode discountCode2;
    {
        discountManager = DiscountManager.getInstance();
        User ali = new User("ali008", "124345", "Ali",
                "Alavi", "Ali@gmail.com", "092000000", new Cart());
        User reza = new User("reza12", "125423", "Reza",
                "Rezaei", "reza@gmail.com", "0913232343", new Cart());
        HashMap<User, Integer> users = new HashMap<>();
        users.put(ali, 2);
        users.put(reza, 1);
        discountCode = new DiscountCode(new Date(), new Date(2020, Calendar.MAY, 1), 10, 10, users);
        discountCode.setCode("Dis#12");
        discountCode1 = new DiscountCode(new Date(2020, Calendar.MARCH, 1), new Date(2020, Calendar.MARCH, 4), 10, 10, users);
        discountCode1.setCode("Dis#13");
        discountCode2 = new DiscountCode(new Date(2019, Calendar.MARCH, 3), new Date(2021, Calendar.MARCH, 1), 10, 10, users);
        discountCode2.setCode("Dis#14");
        discountManager.getDiscountCodes().add(discountCode);
        discountManager.getDiscountCodes().add(discountCode1);
        discountManager.getDiscountCodes().add(discountCode2);
    }
    @Test
    public void getInstanceTest() {
        DiscountManager test = DiscountManager.getInstance();
        Assert.assertEquals(discountManager, test);
    }
    @Test
    public void getDiscountByCodeTest() {
        DiscountCode actual = discountManager.getDiscountByCode("Dis#12");
        Assert.assertEquals(discountCode, actual);
        actual = discountManager.getDiscountByCode("Dis#10");
        Assert.assertNull(actual);
    }
    @Test
    public void isDiscountAvailableTest() {
        boolean successful = discountManager.isDiscountAvailable("Dis#13");
        Assert.assertFalse(successful);
        successful = discountManager.isDiscountAvailable("Dis#12");
        Assert.assertTrue(successful);
    }
    @Test
    public void removeDiscountTest() {
        discountManager.removeDiscount("Dis#12");
        DiscountCode discountCode = discountManager.getDiscountByCode("Dis#12");
        Assert.assertNull(discountCode);
    }
    @Test
    public void editDiscountStartingDateTest() {
        Date newDate = new Date(2020, Calendar.MARCH, 15);
        discountManager.editDiscountStartingDate("Dis#13", newDate);
        Date actual = discountManager.getDiscountByCode("Dis#13").getStartTime();
        Assert.assertEquals(newDate, actual);
    }
    @Test
    public void editDiscountEndingDateTest() {
        Date newDate = new Date(2020, Calendar.MARCH, 5);
        discountManager.editDiscountEndingDate("Dis#13", newDate);
        Date actual = discountManager.getDiscountByCode("Dis#13").getEndTime();
        Assert.assertEquals(newDate, actual);
    }
    @Test
    public void editDiscountOffPercentage() {
        int newPercentage = 20;
        discountManager.editDiscountOffPercentage("Dis#13", newPercentage);
        int actual = discountManager.getDiscountByCode("Dis#13").getOffPercentage();
        Assert.assertEquals(newPercentage, actual);
    }
    @Test
    public void editDiscountMaxDiscountTest() {
        long newMaxDiscount = 1000;
        discountManager.editDiscountMaxDiscount("Dis#14", newMaxDiscount);
        long actual = discountManager.getDiscountByCode("Dis#14").getMaxDiscount();
        Assert.assertEquals(newMaxDiscount, actual);
    }
    @Test
    public void addUserToDiscountCodeUsersTest() {
        User javad = new User("java", "12@1$", "javad",
                "javadi", "javad@gmail.com", "0913345256", new Cart());
        discountManager.addUserToDiscountCodeUsers("Dis#13", javad, 4);
        boolean successful = discountManager.getDiscountByCode("Dis#13").getUsers().containsKey(javad);
        Assert.assertTrue(successful);
    }
    @Test
    public void removeUserFromDiscountCodeUsersTest() {
        User taghi = new User("tagh", "12%$#", "Taghi",
                "Taghavi", "taghi@gmail.com", "0912235433", new Cart());
        User naghi = new User("nagh", "12#2", "Naghi",
                "Naghavi", "naghi@gmail.com", "0912874556", new Cart());
        discountManager.getDiscountByCode("Dis#14").getUsers().put(taghi, 13);
        discountManager.getDiscountByCode("Dis#14").getUsers().put(naghi, 3);
        discountManager.removeUserFromDiscountCodeUsers("Dis#14", taghi);
        boolean successful = discountManager.getDiscountByCode("Dis#14").getUsers().containsKey(taghi);
        Assert.assertFalse(successful);
    }
    @Test
    public void showAllDiscountCodes() {
        List<DiscountCode> expectedAllDiscountCodes = discountManager.getDiscountCodes();
        List<DiscountCode> actualAllDiscountCodes = discountManager.showAllDiscountCodes();
        Assert.assertEquals(actualAllDiscountCodes, expectedAllDiscountCodes);
    }
    @Test
    public void showDiscountCodeTest() {
        DiscountCode expectedDiscountCode = discountManager.getDiscountCodes().get(discountManager.getDiscountCodes().size() - 1);
        DiscountCode actualDiscountCode = discountManager.showDiscountCode("Dis#14");
        Assert.assertEquals(expectedDiscountCode, actualDiscountCode);
    }
}
