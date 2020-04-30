package ModelPackage.System;

import ModelPackage.Users.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private List<Cart> allCarts;
    private static CartManager cartManager = new CartManager();

    public static CartManager getInstance() {
        return cartManager;
    }

    private CartManager() {
        this.allCarts = new ArrayList<>();
    }
}
