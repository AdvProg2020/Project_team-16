package ModelPackage.System.database;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
//import ModelPackage.System.CategoryManager;
import ModelPackage.System.ProductManager;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static <T> T load(Class<T> type, Serializable serializable){
         Session session = HibernateUtil.getSessionFactory().openSession();
         session.beginTransaction();
         T object = session.get(type,serializable);
         if (object == null){

         }
         session.getTransaction().commit();
         return object;
    }

    public static void save(Object object){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();
    }

    public static void delete(Object object){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.remove(object);
        session.getTransaction().commit();
    }

    public static void InitialLoad(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        ProductManager.getInstance().setAllProducts((ArrayList<Product>)loadAllData(Product.class,session));
        //CategoryManager.getInstance().setAllCategories((ArrayList<Category>)loadAllData(Category.class,session));
    }

    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }
}
