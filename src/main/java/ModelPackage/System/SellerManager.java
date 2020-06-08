package ModelPackage.System;


import ModelPackage.Log.SellLog;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;

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
        return seller.getProducts();
    }

    public List<Seller> viewSellersOfProduct (int productId)
            throws NoSuchAProductException {
        Product product = productManager.findProductById(productId);
        return product.getAllSellers();
    }

    public void getMoneyFromSale(Cart cart){
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
