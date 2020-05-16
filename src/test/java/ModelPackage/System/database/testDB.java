package ModelPackage.System.database;

import ModelPackage.Product.Product;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

public class testDB {

    @Test
    public void hibernateUtilTest(){
        Session session = HibernateUtil.getSession();
        Product product = new Product();
        product.setName("Pen");
        session.getTransaction().begin();
        session.saveOrUpdate(product);
        session.getTransaction().commit();
        HibernateUtil.shutdown();
    }

    @Test
    public void loadTest(){
        Product product = DBManager.load(Product.class, 1);
        Assert.assertEquals("Adidas Super Bowl",product.getName());
        product = DBManager.load(Product.class, 1);
        Assert.assertEquals("Adidas Super Bowl",product.getName());
    }
}
