package ModelPackage.System.database;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;


public class DBManager {
    public static <T> T load(Class<T> type, Serializable serializable){
        Session session = HibernateUtil.getSession();
        return session.get(type, serializable);
    }

    public static void save(Object object){
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();
    }

    public static void delete(Object object){
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.remove(object);
        session.getTransaction().commit();
    }

    public static <T> List<T> loadAllData(Class<T> type) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }
}
