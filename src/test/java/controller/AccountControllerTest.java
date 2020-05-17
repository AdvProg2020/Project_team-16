package controller;

import ModelPackage.Product.Company;
import ModelPackage.System.AccountManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import controler.AccountController;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;

import java.util.ArrayList;

public class AccountControllerTest {
    AccountController accountController = new AccountController();

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

        adidas = new Company("Adidas", "115", "Clothing", new ArrayList<>());

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


}
