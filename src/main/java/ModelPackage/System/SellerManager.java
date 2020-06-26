package ModelPackage.System;


import ModelPackage.Log.SellLog;
import ModelPackage.Off.Off;
import ModelPackage.Product.Company;
import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SellerManager {
    private static SellerManager sellerManager = null;
    private SellerManager(){    }
    public static SellerManager getInstance(){
        if (sellerManager == null)
            sellerManager = new SellerManager();
        return sellerManager;
    }

    private ProductManager productManager = ProductManager.getInstance();

    public Company viewCompanyInformation(String username) throws UserNotAvailableException {
        Seller seller = DBManager.load(Seller.class,username);
        if (seller == null) {
            throw new UserNotAvailableException();
        }
        return seller.getCompany();
    }

    public List<SellLog> viewSalesHistory(String username) throws UserNotAvailableException {
        Seller seller = DBManager.load(Seller.class,username);
        if (seller == null) {
            throw new UserNotAvailableException();
        }
        return seller.getSellLogs();
    }

    public List<Product> viewProducts(String username) throws UserNotAvailableException {
        Seller seller = DBManager.load(Seller.class,username);
        if (seller == null) {
            throw new UserNotAvailableException();
        }
        List<Product> toReturn = new ArrayList<>();
        if (seller.getPackages().size() != 0)
            seller.getPackages().forEach(sellPackage -> toReturn.add(sellPackage.getProduct()));
        return toReturn;
    }

    public void deleteProductForSeller(String username, int productId) throws NoSuchAPackageException {
        Seller seller = DBManager.load(Seller.class, username);
        SellPackage sellPackage = seller.findPackageByProductId(productId);
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
        deleteAllSubCarts(seller, product);
        sellPackage.setProduct(null);
        sellPackage.setSeller(null);
        DBManager.save(product);
        DBManager.save(seller);
        DBManager.delete(sellPackage);
    }

    private void deleteAllSubCarts(Seller seller, Product product) {
        List<SubCart> subCarts = getAllSubCarts(seller, product);
        subCarts.forEach(subCart -> {
            Cart cart = subCart.getCart();
            cart.getSubCarts().remove(subCart);
            DBManager.save(cart);
            DBManager.delete(subCart);
        });
    }

    private List<SubCart> getAllSubCarts(Seller seller, Product product) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<SubCart> criteriaQuery = criteriaBuilder.createQuery(SubCart.class);
        Root<SubCart> root = criteriaQuery.from(SubCart.class);
        criteriaQuery.select(root);
        Predicate[] predicates = {
                criteriaBuilder.equal(root.get("seller").as(Seller.class), seller),
                criteriaBuilder.equal(root.get("product").as(Product.class), product)
        };
        criteriaQuery.where(
                predicates
        );
        Query<SubCart> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Seller> viewSellersOfProduct (int productId)
            throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        List<Seller> sellers = new ArrayList<>();
        product.getPackages().forEach(sellPackage -> sellers.add(sellPackage.getSeller()));
        return sellers;
    }

    public void getMoneyFromSale(Cart cart) throws NoSuchAPackageException {
        Seller seller;
        int off;
        long price;
        long balance;

        for (SubCart subCart : cart.getSubCarts()) {
            seller = subCart.getSeller();
            price = CSCLManager.getInstance().findPrice(subCart);
            off = getOff(seller, subCart);
            balance = (long) (seller.getBalance() + price * subCart.getAmount() * (double) (100 - off) / 100);
            seller.setBalance(balance);
            DBManager.save(seller);
        }
    }

    private int getOff(Seller seller, SubCart subCart) {
        for (SellPackage product : subCart.getProduct().getPackages()){
            if (product.getSeller().equals(seller)){
                if (product.isOnOff()){
                    return product.getOff().getOffPercentage();
                }
            }
        }
        return 0;
    }

    public void addASellLog(SellLog sellLog,Seller seller){
        seller.getSellLogs().add(sellLog);
        DBManager.save(seller);
    }
}
