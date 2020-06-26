package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.Product.SoldProduct;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.System.exeption.account.SecondManagerByUserException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.*;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ManagerManager {
    private static ManagerManager managerManager = null;
    private ManagerManager(){

    }
    public static ManagerManager getInstance(){
        if (managerManager == null)
            managerManager = new ManagerManager();
        return managerManager;
    }

    AccountManager accountManager = AccountManager.getInstance();

    public void createManagerProfile(String[] info) {
        Manager manager = accountManager.createManager(info);
        DBManager.save(manager);
    }

    public void deleteUser(String username) throws UserNotAvailableException {
        User user = AccountManager.getInstance().getUserByUsername(username);
        if (user instanceof Seller) {
            deleteSeller(username);
        }
    }

    private void deleteSeller(String username) {
        Seller seller = DBManager.load(Seller.class, username);
        deletePackages(seller);
        deleteAllOffs(seller);
        seller.setOffs(new ArrayList<>());
        deleteAllRequests(seller);
        seller.setRequests(new ArrayList<>());
        deleteAllSubCarts(seller);
        seller.setCart(null);
        seller.setCompany(null);
        DBManager.delete(seller);
    }

    private void deleteAllSubCarts(Seller seller) {
        List<SubCart> subCarts = getAllSubCarts(seller);
        subCarts.forEach(subCart -> {
            Cart cart = subCart.getCart();
            cart.getSubCarts().remove(subCart);
            DBManager.save(cart);
            DBManager.delete(subCart);
        });
    }

    private List<SubCart> getAllSubCarts(Seller seller) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<SubCart> criteriaQuery = criteriaBuilder.createQuery(SubCart.class);
        Root<SubCart> root = criteriaQuery.from(SubCart.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.equal(root.get("seller").as(Seller.class), seller)
        );
        Query<SubCart> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private void deleteAllRequests(Seller seller) {
        List<Request> requests = getAllRequests(seller);
        requests.forEach(request -> {
            request.setSeller(null);
            DBManager.save(request);
        });
    }

    private List<Request> getAllRequests(Seller seller) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.equal(root.get("seller").as(Seller.class), seller)
        );
        Query<Request> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    private void deleteAllOffs(Seller seller) {
        List<Off> offs = new CopyOnWriteArrayList<>(seller.getOffs());
        OffManager offManager = OffManager.getInstance();
        for (Off off : offs) {
            offManager.deleteOff(off);
        }
    }

    private void deletePackages(Seller seller) {
        List<SellPackage> sellPackages = new CopyOnWriteArrayList<>(seller.getPackages());
        for (SellPackage sellPackage : sellPackages) {
            deletePackage(seller, sellPackage);
        }
    }

    private void deletePackage(Seller seller, SellPackage sellPackage) {
        if (sellPackage.isOnOff()) {
            Off off = sellPackage.getOff();
            off.getProducts().remove(sellPackage.getProduct());
            DBManager.save(off);
            sellPackage.setOff(null);
        }
        List<SellPackage> packages = seller.getPackages();
        packages.remove(sellPackage);
        seller.setPackages(new ArrayList<>(packages));
        Product product = sellPackage.getProduct();
        List<SellPackage> sellPackages = product.getPackages();
        sellPackages.remove(sellPackage);
        product.setPackages(new ArrayList<>(sellPackages));
        sellPackage.setProduct(null);
        sellPackage.setSeller(null);
        DBManager.save(product);
        DBManager.save(seller);
        DBManager.delete(sellPackage);
    }

    public void checkIfIsTheFirstManager() throws SecondManagerByUserException {
        List<Manager> list = DBManager.loadAllData(Manager.class);
        if (!list.isEmpty()){
            throw new SecondManagerByUserException();
        }
    }
}
