package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.SellLog;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Product.SoldProduct;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import ModelPackage.Users.User;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SellerManagerTest {
    private SellerManager sellerManager;
    private Seller marmof;
    private User hatam;
    private Company adidas;
    private SellLog dullSellLog;
    private SellLog skirtSellLog;
    private Product dullForKimmi;
    private Product skirtForKimmi;
    private List<SellLog> sellLogs;
    private List<Product> products;
    private Category category;
    private Cart cart;
    private SubCart dull;
    private SubCart skirt;

    {
        adidas = new Company("Adidas","115", "Clothing",new ArrayList<>());

        sellerManager = SellerManager.getInstance();

        marmof = new Seller(
                "marmofayezi",
                "marmof.ir",
                "Cyrus",
                "Statham",
                "marmof@gmail.com",
                "+1 992 1122",
                new Cart(),
                adidas,
                0
        );
        hatam = new User(
                "hatam008",
                "hatapass",
                "Ali",
                "Hatami",
                "hatam008@gmail.com",
                "+98 992 1122",
                new Cart()
        );

        dullForKimmi = new Product();

        List<SellerIntegerMap> prices = new ArrayList<>();

        SellerIntegerMap price = new SellerIntegerMap();
        price.setInteger(10000);
        price.setSeller(marmof);

        dullForKimmi.setPrices(prices);

        skirtForKimmi = new Product();

        products = new ArrayList<>();
        products.add(dullForKimmi);
        products.add(skirtForKimmi);

        dullForKimmi.setAllSellers(new ArrayList<>());
        dullForKimmi.getAllSellers().add(marmof);

        SoldProduct soldDull = new SoldProduct();
        soldDull.setSoldPrice(10000);
        soldDull.setSourceId(dullForKimmi.getId());

        SoldProduct soldSkirt = new SoldProduct();
        soldDull.setSoldPrice(30000);
        soldDull.setSourceId(skirtForKimmi.getId());

        dullSellLog = new SellLog(
                soldDull,
                100000,
                5,
                hatam,
                new Date(),
                DeliveryStatus.DELIVERED
        );
        skirtSellLog = new SellLog(
                soldSkirt,
                300000,
                7,
                hatam,
                new Date(),
                DeliveryStatus.DELIVERED
        );

        sellLogs = new ArrayList<>();
        sellLogs.add(dullSellLog);
        sellLogs.add(skirtSellLog);

        marmof.setSellLogs(sellLogs);

        marmof.setProducts(products);

        category = new Category("stuffForKimmi",null);

        cart = new Cart();
        dull = new SubCart(dullForKimmi, marmof,5);
        skirt = new SubCart(skirtForKimmi, marmof,3);
        cart.getSubCarts().add(dull);
        cart.getSubCarts().add(skirt);

    }


    @Test
    public void viewCompanyInformation() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            User getUserByUsername(String username){
                return marmof;
            }
        };

        Company actualCompany = sellerManager.viewCompanyInformation("marmofayezi");
        Assert.assertEquals(adidas,actualCompany);
    }

    @Test
    public void viewSalesHistory() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            User getUserByUsername(String username){
                return marmof;
            }
        };

        List<SellLog> actualSellLogs = sellerManager.viewSalesHistory("marmofayezi");

        Assert.assertArrayEquals(sellLogs.toArray(),actualSellLogs.toArray());
    }

    @Test
    public void viewProducts() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            User getUserByUsername(String username){
                return marmof;
            }
        };
        new MockUp<ProductManager>(){
            @Mock
            public Product findProductById(String id){
                if (id.equals(dullForKimmi.getId())){
                    return dullForKimmi;
                } else {
                    return skirtForKimmi;
                }
            }
        };

        List<Product> actualProducts = sellerManager.viewProducts("marmofayezi");

        Assert.assertArrayEquals(products.toArray(),actualProducts.toArray());
    }

    @Test
    public void viewSellersOfProduct() throws NoSuchAProductException {
        new MockUp<ProductManager>(){
            @Mock
            public Product findProductById(int id){
                return dullForKimmi;
            }
        };

        List<Seller> actualSellers = sellerManager.viewSellersOfProduct(dullForKimmi.getId());

        Assert.assertArrayEquals(dullForKimmi.getAllSellers().toArray(),actualSellers.toArray());
    }

    @Test
    public void getMoneyFromSale(){
        new MockUp<DBManager>(){
            @Mock
            public void save(Object o) {
            }
        };
        new MockUp<CSCLManager>(){
            @Mock
            int findPrice(SubCart subCart){
                if (subCart.getAmount() == 5){
                    return 10000;
                } else {
                    return 30000;
                }
            }
        };

        sellerManager.getMoneyFromSale(cart);
        Assert.assertEquals(140000,marmof.getBalance());
    }

    @Test
    public void addSellLog() {
        sellerManager.addASellLog(dullSellLog, marmof);
        sellerManager.addASellLog(skirtSellLog, marmof);

        Assert.assertArrayEquals(sellLogs.toArray(), marmof.getSellLogs().toArray());
    }
}
