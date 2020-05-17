package ModelPackage.System;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.cart.ProductExistedInCart;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import org.infinispan.factories.annotations.Inject;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class CartManagerTest {
    @Inject private CartManager cartManager;
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
        List<SellerIntegerMap> stock = new ArrayList<>();
        stock.add(new SellerIntegerMap(seller, 3));
        List<SellerIntegerMap> prices = new ArrayList<>();
        prices.add(new SellerIntegerMap(seller, 1000));
        product = new Product("shoes", "Adidas", sellers, new Category(),
                new HashMap<>(), new HashMap<>(), "high quality", stock,
                prices);
        product.setDateAdded(new Date(2018, Calendar.MARCH, 1));


        List<SellerIntegerMap> prices2 = new ArrayList<>();
        prices2.add(new SellerIntegerMap(seller, 2000));
        Product product2 = new Product("gloves", "Adidas", sellers, new Category(),
                new HashMap<>(), new HashMap<>(), "low quality", stock,
                prices2);
        product2.setDateAdded(new Date(2018, Calendar.MARCH, 1));
        product.setId(12);
        product2.setId(13);
        Category category = new Category();
        //categoryManager.addProductToCategory(product, category);
        //CategoryManager.getInstance().addProductToCategory(product2, category);


        cart = new Cart();
        subCart = new SubCart(product, seller, 2);
        subCart2 = new SubCart(product2, seller, 4);
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
        // TODO : mock for productManager.findProductById & accountManager.getUserByUserName & DBManager
        cartManager.addProductToCart(customer.getCart(), seller.getUsername(),
                product.getId(), 2);
        Assert.assertEquals(customer.getCart().getSubCarts().get(customer.getCart().getSubCarts().size() - 1),
                subCart);
    }
    @Test
    public void addProductToCartCalculateTotalScoreTest() throws Exception{
        cartManager.addProductToCart(customer1.getCart(), "ali110", 12, 2);
        cartManager.addProductToCart(customer1.getCart(), "ali110", 13, 1);
        Assert.assertEquals(4000, customer1.getCart().getTotalPrice());
    }
    @Test(expected = ProductExistedInCart.class)
    public void addProductToCartExistedExcTest() throws Exception{
        cartManager.addProductToCart(customer.getCart(), "ali110", 12, 1);
        cartManager.addProductToCart(customer.getCart(), "ali110", 12, 2);
    }
    @Test(expected = NotEnoughAmountOfProductException.class)
    public void addProductToCartNotEnoughAmountExcTest() throws Exception{
        cartManager.addProductToCart(customer.getCart(), "ali110", 12, 5);
    }
    @Test
    public void getSubCartByProductIdTest() throws Exception{
        SubCart actualSubCart = cartManager.getSubCartByProductId(cart, 12);
        SubCart expectedSubCart = subCart;
        Assert.assertEquals(expectedSubCart, actualSubCart);
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void getSubCartByProductIdNotFoundExcTest() throws Exception{
        cartManager.getSubCartByProductId(cart, 10);
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void deleteProductFromCartTest() throws NoSuchAProductInCart {
        cartManager.deleteProductFromCart(cart, 12);
        cartManager.getSubCartByProductId(cart, 12);
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void deleteProductFromCartNotFoundExcTest() throws Exception{
        cartManager.deleteProductFromCart(cart, 10);
    }
    @Test
    public void changeProductAmountInCartTest() throws Exception {
        cartManager.changeProductAmountInCart(cart, 13, "ali110", 2);
        int actualAmount = cartManager.getSubCartByProductId(cart, 13).getAmount();
        Assert.assertEquals(2, actualAmount);
    }
    @Test
    public void changeProductAmountInCartCalculateTotalPriceTest() throws Exception {
        cartManager.changeProductAmountInCart(cart, 13, "ali110", 3);
        Assert.assertEquals(8000, cart.getTotalPrice());
    }
    @Test(expected = NoSuchAProductInCart.class)
    public void changeProductAmountNotFoundExcTest() throws Exception{
        cartManager.changeProductAmountInCart(cart, 14, "ali110", 2);
    }
    @Test(expected = NotEnoughAmountOfProductException.class)
    public void changeProductAmountInCartNotEnoughExcTest() throws Exception{
        cartManager.changeProductAmountInCart(cart, 13, "ali110", 5);
    }
}
