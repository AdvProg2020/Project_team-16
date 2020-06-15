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
import ModelPackage.System.editPackage.*;
import ModelPackage.Users.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static Session session;
    private static Transaction transaction;

    private static SessionFactory buildSessionFactory() {
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
                    .addAnnotatedClass(CategoryEditAttribute.class)
                    .addAnnotatedClass(DiscountCodeEditAttributes.class)
                    .addAnnotatedClass(EditAttributes.class)
                    .addAnnotatedClass(OffChangeAttributes.class)
                    .addAnnotatedClass(ProductEditAttribute.class)
                    .addAnnotatedClass(UserEditAttributes.class)
                    .addAnnotatedClass(SellPackage.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        transaction.commit();
        session.close();
        sessionFactory.close();
    }

    public static void startUtil() {
        sessionFactory = buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    public static Session getSession() {
        return session;
    }
}
