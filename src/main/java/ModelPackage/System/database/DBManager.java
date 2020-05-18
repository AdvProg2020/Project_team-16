package ModelPackage.System.database;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.Product.ProductStatus;
import ModelPackage.System.CategoryManager;
import ModelPackage.System.ProductManager;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class DBManager {
    public static <T> T load(Class<T> type, Serializable serializable){
         Session session = HibernateUtil.getSession();
         T object = session.get(type,serializable);
         if (object == null){

         }
         return object;
    }

    public static void save(Object object){
        Session session = HibernateUtil.getSession();
        session.saveOrUpdate(object);
    }

    public static void delete(Object object){
        Session session = HibernateUtil.getSession();
        session.remove(object);
    }

    public static void initialLoad(){
        List<Product> list = loadAllData(Product.class);
        Iterator<Product> iterator = list.iterator();
        while (iterator.hasNext()){
            Product toRemove = iterator.next();
            if (!(toRemove.getProductStatus() == ProductStatus.VERIFIED)) list.remove(toRemove);
        }
        ProductManager.getInstance().setAllProductsActive(list);
        CategoryManager.getInstance().setAllCategories(loadAllData(Category.class));
    }

    public static <T> List<T> loadAllData(Class<T> type) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }


}
