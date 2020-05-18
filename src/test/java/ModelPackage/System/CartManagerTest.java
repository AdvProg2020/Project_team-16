package ModelPackage.System;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.cart.ProductExistedInCart;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.*;
import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class CartManagerTest {
    private CartManager cartManager;
    private Cart cart;
    private SubCart subCart;
    private Product product;
    private Product product1;
    private Seller seller;


    @Before
    public void create() {
        cartManager = CartManager.getInstance();
        cart = new Cart();
        seller = new Seller("ali110", "1234", "Ali", "Alavi",
                "Ali@gmail.com", "1223454", new Cart(),
                new Company("Adidas", "12345", "Sports"), 12);
        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        List<SellerIntegerMap> stock = new ArrayList<>();
        stock.add(new SellerIntegerMap(seller, 3));
        List<SellerIntegerMap> prices = new ArrayList<>();
        prices.add(new SellerIntegerMap(seller, 1000));
        product = new Product("Sporting", "Adidas", sellers, new Category(),
                new HashMap<>(), new HashMap<>(),
                "Bad", stock, prices);
        product.setId(1);
        subCart = new SubCart(product, new Seller(), 2);
        cart.getSubCarts().add(subCart);

        product1 = new Product("Shoes", "Adidas", sellers, new Category(),
                new HashMap<>(), new HashMap<>(),
                "Good", stock, prices);
        product1.setId(2);
    }

    @Test
    public void getInstanceTest() {
        CartManager test = CartManager.getInstance();
        Assert.assertEquals(test, cartManager);
    }

    @Test
    public void getSubCartByProductIdTest() throws NoSuchAProductInCart {
        SubCart actualSubCart = cartManager.getSubCartByProductId(cart, 1);
        SubCart expectedSubCart = subCart;
        Assert.assertEquals(expectedSubCart, actualSubCart);
    }

    @Test(expected = NoSuchAProductInCart.class)
    public void getSubCartByProductIdNotFoundProductTest() throws NoSuchAProductInCart {
        cartManager.getSubCartByProductId(cart, 31);
    }

    @Test
    public void addProductToCartTest(@Mocked ProductManager productManager, @Mocked AccountManager accountManager)
            throws ProductExistedInCart, NotEnoughAmountOfProductException,
            NoSuchAProductException, UserNotAvailableException {
        new Expectations() {{
            productManager.findProductById(2);
            result = product1;
            accountManager.getUserByUsername("ali110");
            result = seller;
        }};
        cartManager.addProductToCart(cart, "ali110", 2, 1);
        new Verifications() {{
            DBManager.save(cart.getSubCarts().get(1));
            DBManager.save(cart);
        }};
        SubCart subCart = cart.getSubCarts().get(1);
        Assert.assertNotNull(subCart);
    }
    @Test
    public void deleteProductFromCartTest() throws NoSuchAProductInCart {
        int size = cart.getSubCarts().size();
        cartManager.deleteProductFromCart(cart, 1);
        new Verifications() {{
            DBManager.delete(subCart);
            DBManager.save(cart);
        }};
        int newSize = cart.getSubCarts().size();
        Assert.assertEquals(newSize + 1, size);
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void deleteProductFromCartNotFoundExcTest() throws NoSuchAProductInCart {
        cartManager.deleteProductFromCart(cart, 3);
    }
    @Test(expected = ProductExistedInCart.class)
    public void addProductToCartExistedProductTest() throws ProductExistedInCart,
            NotEnoughAmountOfProductException, NoSuchAProductException, UserNotAvailableException {
        cartManager.addProductToCart(cart, "ali110", 1, 2);
    }
    @Test(expected = NotEnoughAmountOfProductException.class)
    public void addProductToCartNotEnoughAmountExcTest(@Mocked ProductManager productManager, @Mocked AccountManager accountManager)
            throws NoSuchAProductException, ProductExistedInCart,
            UserNotAvailableException, NotEnoughAmountOfProductException {
        cart.getSubCarts().remove(subCart);
        new Expectations(){{
             productManager.findProductById(1);
             result = product;
        }};
        cartManager.addProductToCart(cart, "ali110", 1, 5);
    }
    @Test(expected = NoSuchAProductException.class)
    public void addProductToCartNoSuchProductExcTest() throws ProductExistedInCart,
            NotEnoughAmountOfProductException, NoSuchAProductException, UserNotAvailableException {
        cartManager.addProductToCart(cart, "ali110", 4, 3);
    }
    @Test(expected = UserNotAvailableException.class)
    public void addProductToCartUserNotAvailableExcTest(@Mocked ProductManager productManager)
            throws NoSuchAProductException, ProductExistedInCart,
            UserNotAvailableException, NotEnoughAmountOfProductException {
        new Expectations(){{
            productManager.findProductById(2);
            result = product;
        }};
        cartManager.addProductToCart(cart, "ali110", 2, 1);
    }

}
