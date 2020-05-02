package ModelPackage.System.database;

import ModelPackage.Product.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory()
    {
        try {
            return new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Comment.class)
                    .addAnnotatedClass(Score.class)
                    .addAnnotatedClass(Category.class)
                    .addAnnotatedClass(Company.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
