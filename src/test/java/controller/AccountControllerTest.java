package controller;

import ModelPackage.Product.Company;
import ModelPackage.System.AccountManager;
import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import controler.AccountController;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AccountControllerTest {
    AccountController accountController = new AccountController();

    private User marmof;
    private Manager hatam;
    private Seller sapa;
    private Company adidas;

    ArrayList<User> users;

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
    }

    @Test
    public void usernameInitialCheck() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            public boolean isUsernameAvailable(String username){
                return false;
            }
        };
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
    public void login() throws NotVerifiedSeller, UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            public String login(String username, String password){
                return "Seller";
            }
        };

        String actual =  accountController.login("sapa", "sapa.pak");

        Assert.assertEquals("Seller", actual);
    }
}
