package Models;

import ModelPackage.System.AccountManager;
import ModelPackage.Users.Cart;
import ModelPackage.Users.User;
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
}

