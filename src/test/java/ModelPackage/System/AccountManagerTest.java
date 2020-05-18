package ModelPackage.System;

import ModelPackage.Product.Company;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.SameInfoException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.account.WrongPasswordException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import io.reactivex.exceptions.Exceptions;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
                else return sapa;
            }
        };
        new MockUp<DBManager>(){
            @Mock
            public void save(Object o){
            }
        };
    }

    @Test
    public void changeInfo() throws SameInfoException, UserNotAvailableException {
        UserEditAttributes info = new UserEditAttributes();
        info.setNewEmail("ali@gmail.com");
        accountManager.changeInfo("marmof",info);
        String expected = "ali@gmail.com";
        Assert.assertEquals(expected,marmof.getEmail());
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
        User actual = accountManager.getUserByUsername("marmofayezi");
        User expected = marmof;
    }

    @Test public void createAccount_Seller(){
        String[] info = {
                "marmofayezi",
                "marmof.ir",
                "Mohamadreza",
                "Mofayezi",
                "marmof@gmail.com",
                "09121232222",
                "adidas",
                "1000000"
        };
        new MockUp<CSCLManager>(){
            public Company getCompanyByName(String companyName) {
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
        Assert.assertEquals(hatam,actual);
    }

    @Test
    public void login() throws WrongPasswordException, NotVerifiedSeller, UserNotAvailableException {
        new Expectations(){
            {
                DBManager.load(Manager.class, "hatam008");
                result = hatam;
                times = 1;
            }
        };

        String actual = accountManager.login("hatam008","hatam008@kimi");
        Assert.assertEquals("Manager", actual);
    }

    @Test(expected = WrongPasswordException.class)
    public void loginWithWrongPassword() throws WrongPasswordException, NotVerifiedSeller, UserNotAvailableException {
        accountManager.login("hatam008","marmof.com");
    }

    @Test
    public void logout() throws UserNotAvailableException {
        accountManager.logout("marmofayezi");
        boolean actual = marmof.isHasSignedIn();
        Assert.assertFalse(actual);
    }

    @Test
    public void getUserByUsername() throws UserNotAvailableException {
        User actualUser = accountManager.getUserByUsername("marmofayezi");
        Assert.assertEquals(marmof,actualUser);
    }

    @Test(expected = UserNotAvailableException.class)
    public void getUserByUsername_UserNotFound() throws UserNotAvailableException {
        User actualUser = accountManager.getUserByUsername("arash");
    }

    @Test
    public void isUsernameAvailable(){
        boolean actual = accountManager.isUsernameAvailable("marmofayezi");
        Assert.assertTrue(actual);
    }





}

