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
    {
        discountManager = DiscountManager.getInstance();
        User ali = new User("ali008", "124345", "Ali",
                "Alavi", "Ali@gmail.com", "092000000", new Cart());
        User reza = new User("reza12", "125423", "Reza",
                "Rezaei", "reza@gmail.com", "0913232343", new Cart());
        HashMap<User, Integer> users = new HashMap<>();
        users.put(ali, 2);
        users.put(reza, 1);
        discountCode = new DiscountCode("Dis#12", new Date(), new Date(2020, Calendar.MAY, 1), 10, 10, users);
        DiscountManager.getInstance().getDiscountCodes().add(discountCode);
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
}
