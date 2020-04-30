package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class CartManagerTest {
    private CartManager cartManager;
    private Product product;
    private Customer customer;
    private Seller seller;
    {
        cartManager = CartManager.getInstance();
        seller = new Seller("ali110", "12435", "Ali",
                "Alavi", "Ali@gmail.com", "0913235445", new Cart(),
                new Company(), 12);
        customer = new Customer("sapa80", "1232#2", "Sajad", "Paksima", "paksima@yahoo.com", "0920097123", new Cart(), 12);
        ArrayList<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        HashMap<String, Integer> stock = new HashMap<>();
        stock.put(seller.getUsername(), 3);
        product = new Product("show", "Adidas", sellers, "12",
                new HashMap<>(), new HashMap<>(), "high quality", stock,
                new HashMap<>());
    }
    @Test
    public void getInstanceTest() {
        CartManager test = CartManager.getInstance();
        Assert.assertEquals(cartManager, test);
    }
    @Test
    public void addProductToCartTest() {
        cartManager.addProductToCart(customer.getCart(), seller.getUsername(),
                product.getProductId(), 2);
        SubCart subCart = new SubCart(product, product.getProductId(), seller.getUsername(), 2)
        int b = Comparator.comparing(SubCart :: getProduct).thenComparing(SubCart :: getSellerId).
                thenComparing(SubCart :: getProductId).thenComparing(SubCart :: getAmount).
                compare(customer.getCart().getSubCarts().get(customer.getCart().getSubCarts().size() - 1), subCart);
    }
}
