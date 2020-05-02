package ModelPackage.System.database;

import org.hibernate.Session;
import org.junit.Test;

public class testDB {

    @Test
    public void hibernateUtilTest(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        HibernateUtil.shutdown();
    }
}
