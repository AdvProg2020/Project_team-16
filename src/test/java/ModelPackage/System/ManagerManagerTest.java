package ModelPackage.System;

import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.SecondManagerByUserException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Manager;
import ModelPackage.Users.User;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManagerManagerTest {
    private ManagerManager managerManager;
    private ArrayList<Manager> managers;
    private ArrayList<User> users;
    private Manager hatam;
    private User marmof;

    {
        managerManager = ManagerManager.getInstance();

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

        managers = new ArrayList<>();
        managers.add(hatam);

        users = new ArrayList<>();
        users.add(marmof);
        users.add(hatam);
    }

    @Before
    public void initialize(){
        new MockUp<DBManager>(){
            @Mock
            public void delete(Object o) {
                users.remove(marmof);
            }
            @Mock
            public <T> T load(Class<T> type, Serializable username) throws UserNotAvailableException {
                if (username.equals("marmofayezi")) return (T) marmof;
                else if (username.equals("hatam008") && type.getName().equals("ModelPackage.Users.Manager")) return (T) hatam;
                else return null;
            }
        };

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
        new MockUp<DBManager>(){
          @Mock
          public <T> List<T> loadAllData(Class<T> type){
              return (List<T>) users;
          }
        };
        managerManager.deleteUser("marmofayezi");
        List<User> expected = users;
        List<User> actual = DBManager.loadAllData(User.class);
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void checkFirstManager() {
        new MockUp<DBManager>(){
            @Mock
            public <T> List<T> loadAllData(Class<T> type){
                List<Manager> allManagers = new ArrayList<>();
                return (List<T>) allManagers;
            }
        };

        managerManager.checkIfIsTheFirstManager();
    }

    @Test(expected = SecondManagerByUserException.class)
    public void checkFirstManager_SecondManager() {
        new MockUp<DBManager>(){
            @Mock
            public <T> List<T> loadAllData(Class<T> type){
                return (List<T>) managers;
            }
        };

        managerManager.checkIfIsTheFirstManager();
    }
}
