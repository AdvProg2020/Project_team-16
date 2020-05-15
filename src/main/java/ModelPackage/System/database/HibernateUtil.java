package ModelPackage.System.database;

import ModelPackage.Log.Log;
import ModelPackage.Log.PurchaseLog;
import ModelPackage.Log.SellLog;
import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.SoldProductSellerMap;
import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Off.Off;
import ModelPackage.Product.*;
import ModelPackage.Users.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static Session session = sessionFactory.openSession();
    private static SessionFactory buildSessionFactory()
    {
        try {
            return new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Comment.class)
                    .addAnnotatedClass(Score.class)
                    .addAnnotatedClass(Category.class)
                    .addAnnotatedClass(Company.class)
                    .addAnnotatedClass(Log.class)
                    .addAnnotatedClass(PurchaseLog.class)
                    .addAnnotatedClass(SellLog.class)
                    .addAnnotatedClass(DiscountCode.class)
                    .addAnnotatedClass(Off.class)
                    .addAnnotatedClass(SoldProduct.class)
                    .addAnnotatedClass(Cart.class)
                    .addAnnotatedClass(SubCart.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Manager.class)
                    .addAnnotatedClass(Message.class)
                    .addAnnotatedClass(Request.class)
                    .addAnnotatedClass(Seller.class)
                    .addAnnotatedClass(UserIntegerMap.class)
                    .addAnnotatedClass(SellerIntegerMap.class)
                    .addAnnotatedClass(SoldProductSellerMap.class)
                    .addAnnotatedClass(DiscountcodeIntegerMap.class)
                    .addAnnotatedClass(CustomerInformation.class)
                    .addAnnotatedClass(CustomerInformation.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void startTransaction(){
        session.beginTransaction();
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
