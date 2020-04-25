package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Users.Cart;
import ModelPackage.Users.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
}
