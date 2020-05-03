package ModelPackage.System;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ManagerManagerTest {
    private ManagerManager managerManager;
    private List<Manager> managers;
    private Manager hatam;
    private User marmof;

    {
        managerManager = ManagerManager.getInstance();
        managers = managerManager.getManagers();

        hatam = new Manager(
                "hatam008",
                "hatam008@kimi",
                "Ali",
                "Hatami",
                "hatam008@yahoo.com",
                "09121351223",
                new Cart()
        );

        marmof = new User("marmofayezi",
                "marmof.ir",
                "Mohamadreza",
                "Mofayezi",
                "marmof@gmail.com",
                "09121232222",
                new Cart()
        );

        AccountManager.getInstance().getUsers().add(marmof);
    }

    @Test
    public void createManagerProfile(){
        String[] info = {
                "hatam008",
                "hatam008@kimi",
                "Ali",
                "Hatami",
                "hatam008@yahoo.com",
                "09121351223"
        };
        managerManager.createManagerProfile(info);
        Manager actual = managers.get(0);
        Assert.assertEquals(hatam,actual);
    }

    @Test
    public void deleteUser() {
        managerManager.deleteUser("marmofayezi");
        ArrayList<User> expected = new ArrayList<>();
        ArrayList<User> actual = (ArrayList<User>) AccountManager.getInstance().getUsers();
        Assert.assertArrayEquals(new ArrayList[]{expected}, new ArrayList[]{actual});
    }

}
