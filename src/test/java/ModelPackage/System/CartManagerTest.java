package ModelPackage.System;

import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.cart.ProductExistedInCart;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class CartManagerTest {
    private CartManager cartManager;
    private Product product;
    private Customer customer;
    private Seller seller;
    private SubCart subCart;
    private SubCart subCart2;
    private Cart cart;
    private Customer customer1;
    {
        cartManager = CartManager.getInstance();
        seller = new Seller("ali110", "12435", "Ali",
                "Alavi", "Ali@gmail.com", "0913235445", new Cart(),
                new Company("Adidas", "02112234", "Sports"), 12);
        customer = new Customer("sapa80", "1232#2", "Sajad",
                "Paksima", "paksima@yahoo.com", "0920097123",
                new Cart(), 12);
        customer1 = new Customer("papa80", "21343", "Pop", "Poppy",
                "Pop@gmail.com", "09132345", new Cart(), 1);
        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        HashMap<String, Integer> stock = new HashMap<>();
        HashMap<String, Integer> prices = new HashMap<>();
        prices.put(seller.getUsername(), 1000);
        stock.put(seller.getUsername(), 3);
        product = new Product("shoes", "Adidas", sellers, "12",
                new HashMap<>(), new HashMap<>(), "high quality", stock,
                prices);
        product.setDateAdded(new Date(2018, Calendar.MARCH, 1));
        HashMap<String, Integer> prices2 = new HashMap<>();
        prices2.put(seller.getUsername(), 2000);
        Product product2 = new Product("gloves", "Adidas", sellers, "13",
                new HashMap<>(), new HashMap<>(), "low quality", stock,
                prices2);
        product2.setDateAdded(new Date(2018, Calendar.MARCH, 1));
        product.setProductId("Pro#12");
        product2.setProductId("Pro#13");
        ProductManager.getInstance().addProductToList(product);
        ProductManager.getInstance().addProductToList(product2);
        cart = new Cart();
        subCart = new SubCart(product, "Pro#12", "ali110", 2);
        subCart2 = new SubCart(product2, "Pro#13", "ali110", 4);
        cart.getSubCarts().add(subCart);
        cart.getSubCarts().add(subCart2);
    }
    @Test
    public void getInstanceTest() {
        CartManager test = CartManager.getInstance();
        Assert.assertEquals(cartManager, test);
    }
    @Test
    public void addProductToCartTest() throws Exception{
        cartManager.addProductToCart(customer.getCart(), seller.getUsername(),
                product.getId(), 2);
        Assert.assertEquals(customer.getCart().getSubCarts().get(customer.getCart().getSubCarts().size() - 1),
                subCart);
    }
    @Test
    public void addProductToCartCalculateTotalScoreTest() throws Exception{
        cartManager.addProductToCart(customer1.getCart(), "ali110", "Pro#12", 2);
        cartManager.addProductToCart(customer1.getCart(), "ali110", "Pro#13", 1);
        Assert.assertEquals(4000, customer1.getCart().getTotalPrice());
    }
    @Test(expected = ProductExistedInCart.class)
    public void addProductToCartExistedExcTest() throws Exception{
        cartManager.addProductToCart(customer.getCart(), "ali110", "Pro#12", 1);
        cartManager.addProductToCart(customer.getCart(), "ali110", "Pro#12", 2);
    }
    @Test(expected = NotEnoughAmountOfProductException.class)
    public void addProductToCartNotEnoughAmountExcTest() throws Exception{
        cartManager.addProductToCart(customer.getCart(), "ali110", "Pro#12", 5);
    }
    @Test
    public void getSubCartByProductIdTest() throws Exception{
        SubCart actualSubCart = cartManager.getSubCartByProductId(cart, "Pro#12", "ali110");
        SubCart expectedSubCart = subCart;
        Assert.assertEquals(expectedSubCart, actualSubCart);
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void getSubCartByProductIdNotFoundExcTest() throws Exception{
        cartManager.getSubCartByProductId(cart, "Pro#10", "ali110");
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void deleteProductFromCartTest() throws NoSuchAProductInCart {
        cartManager.deleteProductFromCart(cart, "Pro#12", "ali110");
        cartManager.getSubCartByProductId(cart, "Pro#12", "ali110");
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void deleteProductFromCartNotFoundExcTest() throws Exception{
        cartManager.deleteProductFromCart(cart, "Pro#10", "ali110");
    }
    @Test
    public void changeProductAmountInCartTest() throws Exception {
        cartManager.changeProductAmountInCart(cart, "Pro#13", "ali110", 2);
        int actualAmount = cartManager.getSubCartByProductId(cart, "Pro#13", "ali110").getAmount();
        Assert.assertEquals(2, actualAmount);
    }
    @Test
    public void changeProductAmountInCartCalculateTotalPriceTest() throws Exception {
        cartManager.changeProductAmountInCart(cart, "Pro#13", "ali110", 3);
        Assert.assertEquals(8000, cart.getTotalPrice());
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void changeProductAmountNotFoundExcTest() throws Exception{
        cartManager.changeProductAmountInCart(cart, "Pro#14", "ali110", 2);
    }
    @Test(expected = NotEnoughAmountOfProductException.class)
    public void changeProductAmountInCartNotEnoughExcTest() throws Exception{
        cartManager.changeProductAmountInCart(cart, "Pro#13", "ali110", 5);
    }
}
