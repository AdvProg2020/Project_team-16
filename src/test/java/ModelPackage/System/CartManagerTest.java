package ModelPackage.System;

import org.junit.Assert;
import org.junit.Test;

public class CartManagerTest {
    private CartManager cartManager;
    {
        cartManager = CartManager.getInstance();
    }
    @Test
    public void getInstanceTest() {
        CartManager test = CartManager.getInstance();
        Assert.assertEquals(cartManager, test);
    }
}
