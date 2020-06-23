package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Off.Off;
import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.Users.Advertise;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * TimeMachine is a Runnable class that checks Start Date And End Date
 * of DiscountCodes And Offs and handles Activation Of Them.
 * Every 10 seconds it begin its operation
 */
public class TimeMachine implements Runnable {
    @Override
    public void run() {
        while (true) {
            offCheck();
            discountCheck();
            advertiseCheck();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void advertiseCheck() {
        Date yesterday = new Date(new Date().getTime() - 86400000);
        List<Advertise> advertises = getOffAfterNowField(yesterday, "created", Advertise.class);
        ContentManager instance = ContentManager.getInstance();
        advertises.forEach(instance::removeAdvertise);
    }

    private void discountCheck() {
        deleteExpiredCodes();
    }

    private void deleteExpiredCodes() {
        List<DiscountCode> results = getOffAfterNowField(new Date(), "startTime", DiscountCode.class);
        DiscountManager discountManager = DiscountManager.getInstance();
        results.forEach(discountManager::removeDiscount);
    }

    private void offCheck() {
        deleteExpiredOffs();
        activateNewOffs();
    }

    private void activateNewOffs() {
        List<Off> results = getOffAfterNowField(new Date(), "startTime", Off.class);
        results.forEach(this::addOffToProducts);
    }

    private void addOffToProducts(Off off) {
        String seller = off.getSeller().getUsername();
        for (Product product : off.getProducts()) {
            try {
                SellPackage sellPackage = product.findPackageBySeller(seller);
                sellPackage.setOff(off);
                sellPackage.setOnOff(true);
                DBManager.save(sellPackage);
            } catch (NoSuchSellerException ignore) {
            }
        }
    }

    private void deleteExpiredOffs() {
        List<Off> results = getOffAfterNowField(new Date(), "endTime", Off.class);
        OffManager offManager = OffManager.getInstance();
        results.forEach(offManager::deleteOff);
    }

    private <T> List<T> getOffAfterNowField(Date date, String field, Class<T> type) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> root = criteriaQuery.from(type);
        criteriaQuery.select(root).where(
                criteriaBuilder.gt(root.get(field), date.getTime())
        );
        Query<T> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }
}
