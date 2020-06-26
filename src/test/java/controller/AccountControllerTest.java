package controller;

import ModelPackage.Product.Company;
import ModelPackage.System.AccountManager;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.account.WrongPasswordException;
import ModelPackage.Users.*;
import View.PrintModels.UserFullPM;
import controler.AccountController;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountControllerTest {
    AccountController accountController = new AccountController();

    private User marmof;
    private Manager hatam;
    private Seller sapa;
    private Company adidas;
    private ArrayList<User> users;
    private UserFullPM marmofPM;

    {
        marmof = new Customer("marmofayezi",
                "marmof.ir",
                "Mohamadreza",
                "Mofayezi",
                "marmof@gmail.com",
                "09121232222",
                new Cart(),
                10000
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

        adidas = new Company("Adidas", "115", "Clothing", new ArrayList<>());

        sapa = new Seller(
                "sapa",
                "sapa.pak",
                "Sajad",
                "Paksima",
                "sapa@gmail.com",
                "+2 942 6722",
                new Cart(),
                adidas,
                0
        );

        users = new ArrayList<>();

        marmofPM = new UserFullPM(
                "marmofayezi",
                "Mohamadreza",
                "Mofayezi",
                "marmof@gmail.com",
                "09121232222",
                "Customer"
        );
    }

    @Before
    public void initialize(){
        new MockUp<DBManager>() {
            @Mock
            public void save(Object object){}
            @Mock
            public <T>T load(Class<T> type, Serializable id) {
                if (id.equals(marmof.getUsername()))
                    return type.cast(marmof);
                else if (id.equals("hatam008"))
                    return type.cast(hatam);
                else if (id.equals(sapa.getUsername()))
                    return type.cast(sapa);
                return null;
            }
        };
        new MockUp<AccountManager>(){
            @Mock
            public String login(String username, String password) throws WrongPasswordException, UserNotAvailableException, NotVerifiedSeller {
                if (username.equals("sapa")) {
                    if (!sapa.getVerified())
                        throw new NotVerifiedSeller();
                    else if (password.equals(sapa.getPassword()))
                        return "Seller";
                    else if (password.equals("sapa"))
                        throw new WrongPasswordException(password);
                } else if (username.equals("hata"))
                    throw new UserNotAvailableException();
                return "";
            }
        };
    }

    @Test
    public void usernameInitialCheck() throws UserNotAvailableException {
        accountController.usernameInitialCheck("marmof");
    }

    @Test(expected = UserNotAvailableException.class)
    public void usernameInitialCheck_UserNotAvailableException() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            public boolean isUsernameAvailable(String username){
                return true;
            }
        };
        accountController.usernameInitialCheck("marmof");
    }

    @Test
    public void createAccount() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            public void createAccount(String[] info, String type){
                users.add(marmof);
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

        accountController.createAccount(info, "customer");

        Assert.assertTrue(users.contains(marmof));
    }

    @Test
    public void login() throws NotVerifiedSeller, UserNotAvailableException, WrongPasswordException {
        String actual =  accountController.login("sapa", "sapa.pak");
        Assert.assertEquals("Seller", actual);
    }
    @Test(expected = WrongPasswordException.class)
    public void login_WrongPasswordTest() throws NotVerifiedSeller, UserNotAvailableException, WrongPasswordException {
        accountController.login("sapa", "sapa");
    }
    @Test(expected = UserNotAvailableException.class)
    public void login_UserNotAvailableTest() throws NotVerifiedSeller, UserNotAvailableException, WrongPasswordException {
        accountController.login("hata", "sapa");
    }
    @Test(expected = NotVerifiedSeller.class)
    public void login_NotVerifiedTest() throws NotVerifiedSeller, UserNotAvailableException, WrongPasswordException {
        sapa.setVerified(false);
        accountController.login("sapa", sapa.getPassword());
    }


    @Test
    public void viewPersonalInfo() throws UserNotAvailableException {
        new MockUp<AccountManager>() {
            @Mock
            public User viewPersonalInfo(String userName) {
                if (userName.equals("marmofayezi"))
                    return marmof;
                return null;
            }
        };

        UserFullPM actual = accountController.viewPersonalInfo("marmofayezi");
        Assert.assertEquals(marmofPM, actual);
    }
}
