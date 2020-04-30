package ModelPackage.System;

import ModelPackage.System.AccountManager;
import ModelPackage.System.exeption.account.SameInfoException;
import ModelPackage.System.exeption.account.WrongPasswordException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.User;
import mockit.Expectations;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountManagerTest {

    AccountManager accountManager = AccountManager.getInstance();
    ArrayList<User> users = (ArrayList<User>) accountManager.getUsers();

    User marmof;
    Manager hatam;

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
    public void createAccount() throws IOException {
        String[] info = {"a","a","a","a","a","a"};
        accountManager.createAccount(info,"manager");
        User actual = accountManager.getUserByUsername("a");
        User expected = marmof;
        FileReader fileReader = new FileReader("src/main/resources/users.user");
        Scanner scanner = new Scanner(fileReader);
        Assert.assertEquals(scanner.nextLine(),"{\"username\":\"a\",\"password\":\"a\",\"firstName\":\"a\",\"lastName\":\"a\"," +
                "\"email\":\"a\",\"phoneNumber\":\"a\",\"cart\":{\"subCarts\":[],\"totalPrice\":0},\"hasSignedIn\":false}");
        fileReader.close();
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
    public void logout(){
        accountManager.logout("marmofayezi");
        boolean actual = marmof.isHasSignedIn();
        Assert.assertFalse(actual);
    }

    @Test
    public void getUserByName(){
        User actualUser = accountManager.getUserByUsername("marmofayezi");
        Assert.assertEquals(marmof,actualUser);
    }




}

