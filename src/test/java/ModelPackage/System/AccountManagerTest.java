package ModelPackage.System;

import ModelPackage.System.AccountManager;
import ModelPackage.Users.Cart;
import ModelPackage.Users.User;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;

public class AccountManagerTest {

    User user = new User("a","a","a","a","a","a",new Cart());
    AccountManager accountManager = AccountManager.getInstance();

    @Test
    public void changeInfo() {
        AccountManager.getUsers().add(user);
        accountManager.changeInfo("a","password", "b");
        Assert.assertEquals("b",user.getPassword());
    }

    @Test
    public void createAccount(){
        String[] info = {"a","a","a","a","a","a"};
        accountManager.createAccount(info,"manager");
        User actual = AccountManager.getUsers().get(0);
        User expected = user;
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void login(){
        AccountManager.getUsers().add(user);
        boolean actual = accountManager.login("a","b");
        Assert.assertFalse(actual);
    }


}

