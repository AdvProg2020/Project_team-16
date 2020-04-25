package ModelPackage.System;

import ModelPackage.System.AccountManager;
import ModelPackage.Users.Cart;
import ModelPackage.Users.User;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AccountManagerTest {

    AccountManager accountManager = AccountManager.getInstance();
    User user = new User("a","a","a","a","a","a",new Cart());

    @Test
    public void changeInfo() {
        accountManager.getUsers().add(user);
        accountManager.changeInfo("a","password", "b");
        Assert.assertEquals("b",user.getPassword());
    }

    @Test
    public void createAccount() throws IOException {
        String[] info = {"a","a","a","a","a","a"};
        accountManager.createAccount(info,"manager");
        User actual = accountManager.getUserByUsername("a");
        User expected = user;
        FileReader fileReader = new FileReader("src/main/resources/users.user");
        Scanner scanner = new Scanner(fileReader);
        Assert.assertEquals(scanner.nextLine(),"{\"username\":\"a\",\"password\":\"a\",\"firstName\":\"a\",\"lastName\":\"a\"," +
                "\"email\":\"a\",\"phoneNumber\":\"a\",\"cart\":{\"subCarts\":[],\"totalPrice\":0},\"hasSignedIn\":false}");
        fileReader.close();
    }

    @Test
    public void login(){
        accountManager.getUsers().add(user);
        accountManager.login("a","a");
        boolean actual = user.isHasSignedIn();
        Assert.assertTrue(actual);
    }

    @Test
    public void logout(){
        accountManager.getUsers().add(user);
        accountManager.logout("a");
        boolean actual = user.isHasSignedIn();
        Assert.assertFalse(actual);
    }

    @Test
    public void getUserByName(){
        accountManager.getUsers().add(user);
        User actualUser = accountManager.getUserByUsername("a");
        Assert.assertEquals(user,actualUser);
    }



}

