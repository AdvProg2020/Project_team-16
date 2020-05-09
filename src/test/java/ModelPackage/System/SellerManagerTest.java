package ModelPackage.System;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.Log.SellLog;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SellerManagerTest {
    SellerManager sellerManager;
    Seller marmof;
    User hatam;
    Company adidas;
    SellLog dullSellLog;
    SellLog skirtSellLog;
    Product dullForKimmi;
    Product skirtForKimmi;
    List<SellLog> sellLogs;
    List<String> productIds;
    List<Product> products;
    Category category;

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
                10000000
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
        skirtForKimmi = new Product();

        products = new ArrayList<>();
        products.add(dullForKimmi);
        products.add(skirtForKimmi);

        dullForKimmi.setAllSellers(new ArrayList<>());
        dullForKimmi.getAllSellers().add(marmof);

        dullSellLog = new SellLog(
                dullForKimmi,
                100000,
                5,
                hatam,
                new Date(),
                DeliveryStatus.DELIVERED
        );
        skirtSellLog = new SellLog(
                skirtForKimmi,
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

        productIds = new ArrayList<>();
        productIds.add(dullForKimmi.getId());
        productIds.add(skirtForKimmi.getId());

        marmof.setProductIds(productIds);

        category = new Category("stuffForKimmi",null);

    }


    @Test
    public void viewCompanyInformation() {
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
    public void viewSalesHistory(){
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
    public void viewProducts(){
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
    public void viewSellersOfProduct(){
        new MockUp<ProductManager>(){
            @Mock
            public Product findProductById(String id){
                return dullForKimmi;
            }
        };

        List<Seller> actualSellers = sellerManager.viewSellersOfProduct(dullForKimmi.getId());

        Assert.assertArrayEquals(dullForKimmi.getAllSellers().toArray(),actualSellers.toArray());
    }

    @Test
    public void viewAllCategories(){
        List<Category> categories = new ArrayList<>();
        new MockUp<CategoryManager>(){
            @Mock
            public List<Category> getAllCategories(){
                categories.add(category);
                return categories;
            }
        };

        List<Category> actualCategories = sellerManager.viewAllCategories();

        Assert.assertArrayEquals(categories.toArray(),actualCategories.toArray());

    }


}
