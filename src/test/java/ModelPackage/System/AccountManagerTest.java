package ModelPackage.System;

import ModelPackage.Product.Company;
import ModelPackage.System.exeption.account.SameInfoException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.account.WrongPasswordException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class AccountManagerTest {

    private AccountManager accountManager = AccountManager.getInstance();
    private ArrayList<User> users = (ArrayList<User>) accountManager.getUsers();

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

        users.add(marmof);
        users.add(hatam);
    }

    @Test
    public void changeInfo() throws SameInfoException {
        String[] info = {"marmofayezi","password", "marmof.ir", "marmof.com"};
        accountManager.getUsers().add(marmof);
        accountManager.changeInfo(info);
        String expected = "marmof.com";
        Assert.assertEquals(expected,marmof.getPassword());
    }

    @Test(expected = SameInfoException.class)
    public void changeInfoSameInfo() throws SameInfoException {
        String[] info = {"marmofayezi","password", "marmof.ir", "marmof.ir"};
        accountManager.getUsers().add(marmof);
        accountManager.changeInfo(info);
    }

    @Test
    public void createAccount_Customer() {
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
    public void login() throws WrongPasswordException {
        accountManager.login("marmofayezi","marmof.ir");
        boolean actual = marmof.isHasSignedIn();
        Assert.assertTrue(actual);
    }

    @Test(expected = WrongPasswordException.class)
    public void loginWithWrongPassword() throws WrongPasswordException {
        accountManager.login("marmofayezi","marmof.com");
    }

    @Test
    public void logout() {
        accountManager.logout("marmofayezi");
        boolean actual = marmof.isHasSignedIn();
        Assert.assertFalse(actual);
    }

    @Test
    public void getUserByUsername() {
        User actualUser = accountManager.getUserByUsername("marmofayezi");
        Assert.assertEquals(marmof,actualUser);
    }

    @Test(expected = UserNotAvailableException.class)
    public void getUserByUsername_UserNotFound(){
        User actualUser = accountManager.getUserByUsername("arash");
    }

    @Test
    public void isUsernameAvailable(){
        boolean actual = accountManager.isUsernameAvailable("marmofayezi");
        Assert.assertTrue(actual);
    }





}

