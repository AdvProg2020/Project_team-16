package ModelPackage.System;

import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.Users.Cart;
import ModelPackage.Users.User;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DiscountManagerTest {
    private DiscountManager discountManager;
    private DiscountCode superDiscountCode;
    private DiscountCode classicDiscountCode;
    private DiscountCode disCode;
    private DiscountCodeEditAttributes discountCodeEditAttributes;
    private User sapa;
    @Before
    public void create() {
        discountManager = DiscountManager.getInstance();
        superDiscountCode = new DiscountCode("Dis#12", new Date(2012, Calendar.JULY, 1),
                new Date(2030, Calendar.JULY, 1), 10, 12);
        classicDiscountCode = new DiscountCode("Dis#13", new Date(2012, Calendar.JULY, 1),
                new Date(2012, Calendar.JULY, 1), 10, 12);
        disCode = new DiscountCode();
        disCode.setCode("Dis#14");
        new Expectations(){{
             DBManager.save(superDiscountCode);
             DBManager.save(classicDiscountCode);
        }};
        sapa = new User("sapa", "12343", "Sajad", "Paksima",
                "paksima@gmail.com", "12345654", new Cart());
        discountCodeEditAttributes = new DiscountCodeEditAttributes();
        discountCodeEditAttributes.setStart(new Date(2019, Calendar.MARCH, 1));
        discountCodeEditAttributes.setEnd(new Date(2021, Calendar.DECEMBER, 12));
        discountCodeEditAttributes.setMaxDiscount(20);
        discountCodeEditAttributes.setOffPercent(30);
    }

    @Test
    public void getInstanceTest() {
        DiscountManager test = DiscountManager.getInstance();
        Assert.assertEquals(test, discountManager);
    }
    @Test
    public void getDiscountByCodeTest() throws NoSuchADiscountCodeException {
        new Expectations(){{
            DBManager.load(DiscountCode.class, superDiscountCode.getCode());
        }};
        DiscountCode actualDiscount = discountManager.getDiscountByCode("Dis#12");
        Assert.assertEquals(superDiscountCode, actualDiscount);
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void getDiscountByCodeNotFoundExcTest() throws NoSuchADiscountCodeException {
        new Expectations(){{
            DBManager.load(DiscountCode.class, disCode.getCode());
        }};
        discountManager.getDiscountByCode(disCode.getCode());
    }
    // TODO : edit test
    @Test
    public void isDiscountAvailableTest() throws NoSuchADiscountCodeException {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
            discountManager.getDiscountByCode("Dis#13");
            result = classicDiscountCode;
        }};
        boolean successful = discountManager.isDiscountAvailable("Dis#12");
        Assert.assertTrue(successful);
        boolean unsuccessful = discountManager.isDiscountAvailable("Dis#13");
        Assert.assertFalse(unsuccessful);
    }
    @Test
    public void removeDiscountTest() throws NoSuchADiscountCodeException {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        discountManager.removeDiscount("Dis#12");
        Assert.assertNull(DBManager.load(DiscountCode.class, superDiscountCode.getCode()));
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void removeDiscountNotFoundExcTest() throws NoSuchADiscountCodeException {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#14");
            result = disCode;
        }};
        discountManager.removeDiscount("Dis#14");
    }
    @Test
    public void editDiscountTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
        DiscountCode expectedDiscountCode = new DiscountCode("Dis#12", new Date(2019, Calendar.MARCH, 1),
                new Date(2021, Calendar.DECEMBER, 12), 30, 20);
        Assert.assertEquals(expectedDiscountCode.getStartTime(), superDiscountCode.getStartTime());
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void editDiscountNotFoundExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#14");
            result = disCode;
        }};
        discountManager.editDiscountCode("Dis#14", discountCodeEditAttributes);
    }
    @Test(expected = NegativeMaxDiscountException.class)
    public void editDiscountCodeNegativeMaxDiscountExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        discountCodeEditAttributes.setMaxDiscount(-3);
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
    }
    @Test(expected = NotValidPercentageException.class)
    public void editDiscountNotValidPercentageExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        discountCodeEditAttributes.setOffPercent(105);
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
    }
    /*@Test(expected = StartingDateIsAfterEndingDate.class)
    public void editDiscountCodeStartingAfterEndingExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        discountCodeEditAttributes.setEnd(new Date(2012, Calendar.DECEMBER, 1));
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
    }*/
    @Test(expected = StartingDateIsAfterEndingDate.class)
    public void editDiscountCodeStartingAfterEndingExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        discountCodeEditAttributes.setStart(new Date(2023, Calendar.DECEMBER, 1));
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
    }
    @Test
    public void addUserToDiscountCodeUsersTest() throws NoSuchADiscountCodeException,
            UserExistedInDiscountCodeException {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        discountManager.addUserToDiscountCodeUsers("Dis#12", sapa, 1);
        UserIntegerMap map = new UserIntegerMap();
        map.setInteger(1);
        map.setUser(sapa);
        Assert.assertEquals(superDiscountCode.getUsers().get(0).getUser(), sapa);
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void addUserToDisCodeNotFoundDisCodeExcTest() throws NoSuchADiscountCodeException,
            UserExistedInDiscountCodeException {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#14");
            result = disCode;
        }};
        discountManager.addUserToDiscountCodeUsers("Dis#14", sapa, 1);
    }
    @Test(expected = UserExistedInDiscountCodeException.class)
    public void addUserToDisCodeUserExistInListExcTest() throws NoSuchADiscountCodeException,
            UserExistedInDiscountCodeException {
        new Expectations(){{
            discountManager.getDiscountByCode("Dis#12");
            result = superDiscountCode;
        }};
        UserIntegerMap map = new UserIntegerMap();
        map.setInteger(1);
        map.setUser(sapa);
        discountManager.addUserToDiscountCodeUsers("Dis#12", sapa, 1);
        discountManager.addUserToDiscountCodeUsers("Dis#12", sapa, 1);
    }
}
