package ModelPackage.System;

import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.User;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DiscountManagerTest {
    private DiscountManager discountManager;
    private DiscountCode superDiscountCode;
    private DiscountCode classicDiscountCode;
    private DiscountCode disCode;
    private DiscountCodeEditAttributes discountCodeEditAttributes;
    private Customer sapa;
    @Before
    public void create() {
        discountManager = DiscountManager.getInstance();
        superDiscountCode = new DiscountCode("Dis#12", new Date(2012, Calendar.JULY, 1),
                new Date(2030, Calendar.JULY, 1), 10, 12);
        classicDiscountCode = new DiscountCode("Dis#13", new Date(2012, Calendar.JULY, 1),
                new Date(2012, Calendar.JULY, 1), 10, 12);
        disCode = new DiscountCode();
        disCode.setCode("Dis#14");

        FakeDBManager fakeDBManager = new FakeDBManager();
        fakeDBManager.save(superDiscountCode);
        fakeDBManager.save(classicDiscountCode);
        fakeDBManager.save(sapa);

        sapa = new Customer("sapa", "12343", "Sajad", "Paksima",
                "paksima@gmail.com", "12345654", new Cart(), 20);

        discountCodeEditAttributes = new DiscountCodeEditAttributes();
        discountCodeEditAttributes.setStart(new Date(2019, Calendar.MARCH, 1));
        discountCodeEditAttributes.setEnd(new Date(2021, Calendar.DECEMBER, 12));
        discountCodeEditAttributes.setMaxDiscount(20);
        discountCodeEditAttributes.setOffPercent(30);
    }

    @Test
    public void getInstanceTest() {
        DiscountManager test = DiscountManager.getInstance();
        assertEquals(test, discountManager);
    }
    @Test
    public void getDiscountByCodeTest() throws NoSuchADiscountCodeException {
        DiscountCode actualDiscount = discountManager.getDiscountByCode("Dis#12");
        assertEquals(superDiscountCode, actualDiscount);
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void getDiscountByCodeNotFoundExcTest() throws NoSuchADiscountCodeException {
        discountManager.getDiscountByCode("Dis#14");
    }
    // TODO : edit test
    @Test
    public void isDiscountAvailableTest() throws NoSuchADiscountCodeException {
        boolean successful = discountManager.isDiscountAvailable("Dis#12");
        Assert.assertFalse(successful);
        boolean unsuccessful = discountManager.isDiscountAvailable("Dis#13");
        Assert.assertFalse(unsuccessful);
    }
    @Test
    public void removeDiscountTest() throws NoSuchADiscountCodeException {
        discountManager.removeDiscount("Dis#12");
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
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
        DiscountCode expectedDiscountCode = new DiscountCode("Dis#12", new Date(2019, Calendar.MARCH, 1),
                new Date(2021, Calendar.DECEMBER, 12), 30, 20);
        assertEquals(expectedDiscountCode.getStartTime(), superDiscountCode.getStartTime());
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void editDiscountNotFoundExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        discountManager.editDiscountCode("Dis#14", discountCodeEditAttributes);
    }
    @Test(expected = NegativeMaxDiscountException.class)
    public void editDiscountCodeNegativeMaxDiscountExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        discountCodeEditAttributes.setMaxDiscount(-3);
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
    }
    @Test(expected = NotValidPercentageException.class)
    public void editDiscountNotValidPercentageExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        discountCodeEditAttributes.setOffPercent(105);
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
    }
    @Test(expected = StartingDateIsAfterEndingDate.class)
    public void editDiscountCodeStartingAfterEndingExcTest() throws NoSuchADiscountCodeException,
            NegativeMaxDiscountException, NotValidPercentageException,
            StartingDateIsAfterEndingDate {
        discountCodeEditAttributes.setStart(new Date(2023, Calendar.DECEMBER, 1));
        discountManager.editDiscountCode("Dis#12", discountCodeEditAttributes);
    }
    @Test
    public void addUserToDiscountCodeUsersTest() throws NoSuchADiscountCodeException,
            UserExistedInDiscountCodeException {
        discountManager.addUserToDiscountCodeUsers("Dis#12", sapa, 1);
        UserIntegerMap map = new UserIntegerMap();
        map.setInteger(1);
        map.setUser(sapa);
        assertEquals(superDiscountCode.getUsers().get(0).getUser(), sapa);
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void addUserToDisCodeNotFoundDisCodeExcTest() throws NoSuchADiscountCodeException,
            UserExistedInDiscountCodeException {
        discountManager.addUserToDiscountCodeUsers("Dis#14", sapa, 1);
    }
    @Test(expected = UserExistedInDiscountCodeException.class)
    public void addUserToDisCodeUserExistInListExcTest() throws NoSuchADiscountCodeException,
            UserExistedInDiscountCodeException {
        UserIntegerMap map = new UserIntegerMap();
        map.setInteger(1);
        map.setUser(sapa);
        discountManager.addUserToDiscountCodeUsers("Dis#12", sapa, 1);
        discountManager.addUserToDiscountCodeUsers("Dis#12", sapa, 1);
    }
    // TODO : think having bug
    @Test
    public void deleteUserFromDiscountCodeUsers() throws UserExistedInDiscountCodeException, NoSuchADiscountCodeException, UserNotExistedInDiscountCodeException {
        UserIntegerMap map = new UserIntegerMap();
        map.setInteger(1);
        map.setUser(sapa);
        discountManager.addUserToDiscountCodeUsers("Dis#12", sapa, 1);
        discountManager.removeUserFromDiscountCodeUsers("Dis#12", sapa);
        assertEquals(superDiscountCode.getUsers().size(), 0);
    }
    @Test(expected = UserNotExistedInDiscountCodeException.class)
    public void deleteUserFromDiscountUsers_NotFoundExcTest() throws UserNotExistedInDiscountCodeException, NoSuchADiscountCodeException {
        discountManager.removeUserFromDiscountCodeUsers("Dis#12", sapa);
    }
    @Test(expected = NoSuchADiscountCodeException.class)
    public void deleteUserFromDiscountUsers_DiscountNotFoundExcTest() throws UserNotExistedInDiscountCodeException, NoSuchADiscountCodeException {
        discountManager.removeUserFromDiscountCodeUsers("Dis#14", sapa);
    }
    @Test
    public void createDiscountCodeTest() throws NotValidPercentageException, StartingDateIsAfterEndingDate, AlreadyExistCodeException {
        discountManager.createDiscountCode("Dis#15", new Date(2020, Calendar.MARCH, 2),
                new Date(2021, Calendar.MARCH, 4), 4, 20);
    }
    @Test(expected = AlreadyExistCodeException.class)
    public void createDiscountCodeTest_AlreadyExists() throws NotValidPercentageException, StartingDateIsAfterEndingDate, AlreadyExistCodeException {
        discountManager.createDiscountCode("Dis#12", new Date(2020, Calendar.MARCH, 2),
                new Date(2021, Calendar.MARCH, 4), 4, 20);
    }
}
