package ModelPackage.System;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Off.Off;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.cart.NotTheSellerException;
import ModelPackage.System.exeption.cart.ProductExistedInCart;
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.*;
import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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


        SellPackage sellPackage = new SellPackage(product, seller, 20, 3, null, false, true);
        product = new Product("Shirt", new Company("Adidas", "12345", "Sports"), new Category(),
                new HashMap<>(), new HashMap<>(), "Bad", sellPackage);

        product.setId(1);
        subCart = new SubCart(product, seller, 1);
        cart.getSubCarts().add(subCart);

        SellPackage sellPackage1 = new SellPackage(product1, seller, 4, 4, null, false, true);
        product1 = new Product("Shoes", new Company("Adidas", "12345", "Sports"), new Category(),
                new HashMap<>(), new HashMap<>(), "Good", sellPackage1);
        product1.setId(2);

        FakeDBManager fakeDBManager = new FakeDBManager();
        fakeDBManager.save(seller);
        fakeDBManager.save(product);
        fakeDBManager.save(product1);
        fakeDBManager.save(subCart);
        fakeDBManager.save(cart);
    }

    @Test
    public void getInstanceTest() {
        CartManager test = CartManager.getInstance();
        assertEquals(test, cartManager);
    }

    @Test
    public void getSubCartByProductIdTest() throws NoSuchAProductInCart {
        SubCart actualSubCart = cartManager.getSubCartByProductId(cart, 1);
        SubCart expectedSubCart = subCart;
        assertEquals(expectedSubCart, actualSubCart);
    }

    @Test(expected = NoSuchAProductInCart.class)
    public void getSubCartByProductIdNotFoundProductTest() throws NoSuchAProductInCart {
        cartManager.getSubCartByProductId(cart, 31);
    }

    @Test
    public void addProductToCartTest(@Mocked ProductManager productManager)
            throws ProductExistedInCart, NotEnoughAmountOfProductException,
            NoSuchAProductException, UserNotAvailableException, NoSuchSellerException, NotTheSellerException, NoSuchAPackageException {
        new Expectations() {{
            productManager.findProductById(2);
            result = product1;
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
    public void changeProductAmount_deleteSubCartTest(@Mocked ProductManager productManager)
            throws NoSuchAProductInCart, NoSuchAProductException, NoSuchSellerException,
            NotEnoughAmountOfProductException, NoSuchAPackageException {
        new Expectations(){{
            cartManager.getSubCartByProductId(cart, 1);
            result = subCart;
            productManager.findProductById(1);
            result = product;
        }};
        cartManager.changeProductAmountInCart(cart, 1, "ali110", -1);
        assertEquals(cart.getSubCarts().size(), 0);
    }
    @Test
    public void changeProductAmountInCartTest(@Mocked ProductManager productManager) throws NoSuchAProductInCart, NoSuchAProductException, NoSuchSellerException, NotEnoughAmountOfProductException, NoSuchAPackageException {
        new Expectations(){{
            cartManager.getSubCartByProductId(cart, 1);
            result = subCart;
            productManager.findProductById(1);
            result = product;
        }};
        cartManager.changeProductAmountInCart(cart, 1, "ali110", 1);
        assertEquals(subCart.getAmount(), 2);
    }
    /*@Test
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
    }*/
    @Test(expected = ProductExistedInCart.class)
    public void addProductToCartExistedProductTest() throws ProductExistedInCart,
            NotEnoughAmountOfProductException, NoSuchAProductException,
            UserNotAvailableException, NoSuchSellerException, NotTheSellerException, NoSuchAPackageException {
        cartManager.addProductToCart(cart, "ali110", 1, 2);
    }
    @Test(expected = NotEnoughAmountOfProductException.class)
    public void addProductToCartNotEnoughAmountExcTest(@Mocked ProductManager productManager)
            throws NoSuchAProductException, ProductExistedInCart,
            UserNotAvailableException, NotEnoughAmountOfProductException, NoSuchSellerException, NotTheSellerException, NoSuchAPackageException {
        cart.getSubCarts().remove(subCart);
        new Expectations(){{
             productManager.findProductById(1);
             result = product;
        }};
        cartManager.addProductToCart(cart, "ali110", 1, 5);
    }
    @Test(expected = NoSuchAProductException.class)
    public void addProductToCartNoSuchProductExcTest(@Mocked ProductManager productManager) throws ProductExistedInCart,
            NotEnoughAmountOfProductException, NoSuchAProductException, UserNotAvailableException,
            NoSuchSellerException, NotTheSellerException, NoSuchAPackageException {
        new Expectations(){{
            DBManager.load(Product.class, 8);
            result = null;
        }};
        cartManager.addProductToCart(cart, "ali110", 8, 1);
    }
    @Test(expected = NoSuchSellerException.class)
    public void addProductToCartUserNotAvailableExcTest(@Mocked ProductManager productManager, @Mocked AccountManager accountManager)
            throws NoSuchAProductException, ProductExistedInCart,
            UserNotAvailableException, NotEnoughAmountOfProductException,
            NoSuchSellerException, NotTheSellerException, NoSuchAPackageException {

        new Expectations(){{
            productManager.findProductById(2);
            result = product;
        }};
        cartManager.addProductToCart(cart, "mohammad", 2, 1);
    }

}
