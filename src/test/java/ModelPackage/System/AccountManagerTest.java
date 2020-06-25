package ModelPackage.System;

import ModelPackage.Product.Company;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.account.*;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import View.exceptions.NotSignedInYetException;
import io.reactivex.exceptions.Exceptions;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountManagerTest {

    private AccountManager accountManager = AccountManager.getInstance();

    private User marmof;
    private Manager hatam;
    private Seller sapa;
    private Company adidas;

    {
        marmof = new User("marmofayezi",
                "marmof.ir",
                "Mohamadreza",
                "Mofayezi",
                "marmof@gmail.com",
                "09121232222",
                new Cart()
        );
        hatam = new Manager(
                "hatam008",
                "hatam008@kimi",
                "Ali",
                "Hatami",
                "hatam008@yahoo.com",
                "09121351223",
                new Cart()
        );

        adidas = new Company("Adidas","115", "Clothing",new ArrayList<>());

        sapa = new Seller(
                "marmofayezi",
                "marmof.ir",
                "Cyrus",
                "Statham",
                "marmof@gmail.com",
                "+1 992 1122",
                new Cart(),
                adidas,
                10000000
        );
    }

    @Before
    public void initialize(){
        new MockUp<AccountManager>(){
            @Mock
            public User getUserByUsername(String username) throws UserNotAvailableException {
                if (username.equals("marmofayezi")) return marmof;
                else if (username.equals("hatam008")) return hatam;
                else throw new UserNotAvailableException();
            }
        };
        new MockUp<DBManager>(){
            @Mock
            public void save(Object o) {
            }
            @Mock
            public <T> T load(Class<T> type, Serializable username) throws UserNotAvailableException {
                if (username.equals("marmofayezi")) return (T) marmof;
                else if (username.equals("hatam008") && type.getName().equals("ModelPackage.Users.Manager")) return (T) hatam;
                else return null;
            }
        };
    }

    @Test
    public void changeInfo() throws SameInfoException, UserNotAvailableException {
        UserEditAttributes info = new UserEditAttributes();
        info.setNewEmail("ali@gmail.com");
        info.setNewFirstName("marmof");
        info.setNewLastName("mofayezi");
        info.setNewPassword("1234");
        info.setNewPhone("+98 21 6420");
        accountManager.changeInfo("marmofayezi",info);
        String expectedEmail = "ali@gmail.com";
        String expectedFirstName = "marmof";
        String expectedLastName = "mofayezi";
        String expectedPassword = "1234";
        String expectedPhone = "+98 21 6420";
        assertEquals(expectedEmail,marmof.getEmail());
        assertEquals(expectedFirstName, marmof.getFirstName());
        assertEquals(expectedLastName, marmof.getLastName());
        assertEquals(expectedPassword, marmof.getPassword());
        assertEquals(expectedPhone, marmof.getPhoneNumber());
    }

    @Test
    public void createAccount_Customer() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            public User getUserByUsername(String username) throws UserNotAvailableException {
                return marmof;
            }
        };
        new MockUp<DBManager>(){
            @Mock
            public void save(Object o){
            }
        };

        String[] info = {
                "marmofayezi",
                "marmof.ir",
                "Mohamadreza",
                "Mofayezi",
                "marmof@gmail.com",
                "09121232222",
                "1000000"
        };
        accountManager.createAccount(info,"customer");
    }

    @Test public void createAccount_Seller(){
        String[] info = {
                "marmofayezi",
                "marmof.ir",
                "Mohamadreza",
                "Mofayezi",
                "marmof@gmail.com",
                "09121232222",
                "0",
                "1000000"
        };
        new MockUp<CSCLManager>(){
            @Mock
            public Company getCompanyById(int id) {
                return adidas;
            }
        };
        accountManager.createAccount(info, "seller");
    }

    @Test
    public void createManager(){
        String[] info = {
                "hatam008",
                "hatam008@kimi",
                "Ali",
                "Hatami",
                "hatam008@yahoo.com",
                "09121351223"
        };
        Manager actual = accountManager.createManager(info);
        assertEquals(hatam,actual);
    }
    @Test
    public void viewPersonalInfoTest() throws UserNotAvailableException {
        User actual = accountManager.viewPersonalInfo("marmofayezi");
        assertEquals(actual, marmof);
    }

    @Test
    public void login() throws WrongPasswordException, NotVerifiedSeller, UserNotAvailableException {
        String actual = accountManager.login("hatam008","hatam008@kimi");
        assertEquals("Manager", actual);
    }

    @Test(expected = WrongPasswordException.class)
    public void loginWithWrongPassword() throws WrongPasswordException, NotVerifiedSeller, UserNotAvailableException {
        accountManager.login("hatam008","marmof.com");
    }

    @Test
    public void logout() throws UserNotAvailableException {
        accountManager.logout("marmofayezi");
    }

    @Test(expected = UserNotAvailableException.class)
    public void logout_NotSignedIn() throws UserNotAvailableException {
        accountManager.logout("arash");
    }

    @Test
    public void getUserByUsername() throws UserNotAvailableException {
        User actualUser = accountManager.getUserByUsername("marmofayezi");
        assertEquals(actualUser, marmof);
    }

    @Test(expected = UserNotAvailableException.class)
    public void getUserByUsername_UserNotFound() throws UserNotAvailableException {
        accountManager.getUserByUsername("arash");
    }

    @Test
    public void isUsernameAvailable(){
        boolean actual = accountManager.isUsernameAvailable("marmofayezi");
        assertTrue(actual);
    }

}

