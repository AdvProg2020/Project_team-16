package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Off.Off;
import ModelPackage.Off.OffStatus;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * TimeMachine is a Runnable class that checks Start Date And End Date
 * of DiscountCodes And Offs and handles Activation Of Them.
 * Every 60 seconds it begin its operation
 */
public class TimeMachine implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("Ran");
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
        List<Advertise> advertises = getValidObjects(yesterday, "created", Advertise.class, true);
        ContentManager instance = ContentManager.getInstance();
        advertises.forEach(instance::removeAdvertise);
    }

    private void discountCheck() {
        deleteExpiredCodes();
    }

    private void deleteExpiredCodes() {
        List<DiscountCode> results = getValidObjects(new Date(), "endTime", DiscountCode.class, true);
        DiscountManager discountManager = DiscountManager.getInstance();
        results.forEach(discountManager::removeDiscount);
    }

    private void offCheck() {
        deleteExpiredOffs();
        activateNewOffs();
    }

    private void activateNewOffs() {
        List<Off> results = getValidObjects(new Date(), "startTime", Off.class, false);
        results.forEach(this::addOffToProducts);
    }

    private void addOffToProducts(Off off) {
        off.setOffStatus(OffStatus.ACTIVATED);
        DBManager.save(off);
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
        List<Off> results = getValidObjects(new Date(), "endTime", Off.class, true);
        OffManager offManager = OffManager.getInstance();
        results.forEach(offManager::deleteOff);
    }

    private <T> List<T> getValidObjects(Date date, String field, Class<T> type, boolean before) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
        Root<T> root = criteriaQuery.from(type);
        criteriaQuery.select(root);
        criteriaQuery.where(
                before ? criteriaBuilder.lessThan(root.get(field).as(Date.class), new Timestamp(date.getTime())) :
                        criteriaBuilder.greaterThan(root.get(field).as(Date.class), new Timestamp(date.getTime()))
        );
        Query<T> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }
}
