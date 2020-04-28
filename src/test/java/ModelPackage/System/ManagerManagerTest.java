package ModelPackage.System;

import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ManagerManagerTest {
    ManagerManager managerManager;
    List<Manager> managers;
    Manager hatam;

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

}
