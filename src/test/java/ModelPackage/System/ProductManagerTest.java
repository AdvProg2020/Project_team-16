package ModelPackage.System;

import org.junit.Assert;
import org.junit.Test;

public class ProductManagerTest {
    private ProductManager productManager;
    {
        productManager = ProductManager.getInstance();
    }

    @Test
    public void getInstanceTest(){
        ProductManager test = ProductManager.getInstance();
        Assert.assertEquals(test,productManager);
    }
}
