package ModelPackage.System;


import ModelPackage.Log.SellLog;
import ModelPackage.Off.Off;
import ModelPackage.Product.Company;
import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;

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
        seller.getPackages().remove(sellPackage);
        DBManager.save(seller);
        Product product = sellPackage.getProduct();
        product.getPackages().remove(sellPackage);
        DBManager.save(product);
        if (sellPackage.isOnOff()) {
            Off off = sellPackage.getOff();
            off.getProducts().remove(sellPackage.getProduct());
            DBManager.save(off);
        }
        sellPackage.setOff(null);
        sellPackage.setProduct(null);
        sellPackage.setSeller(null);
        DBManager.delete(sellPackage);
    }

    public List<Seller> viewSellersOfProduct (int productId)
            throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        List<Seller> sellers = new ArrayList<>();
        product.getPackages().forEach(sellPackage -> sellers.add(sellPackage.getSeller()));
        return sellers;
    }

    public void getMoneyFromSale(Cart cart) throws NoSuchSellerException {
        Seller seller;
        long price;
        for (SubCart subCart : cart.getSubCarts()) {
            seller = subCart.getSeller();
            price = CSCLManager.getInstance().findPrice(subCart);
            seller.setBalance(seller.getBalance() + price * subCart.getAmount());
            DBManager.save(seller);
        }
    }

    public void addASellLog(SellLog sellLog,Seller seller){
        seller.getSellLogs().add(sellLog);
        DBManager.save(seller);
    }
}
