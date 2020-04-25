package ModelPackage.System;

import org.junit.Assert;
import org.junit.Test;

public class DiscountManagerTest {
    private DiscountManager discountManager;
    {
        discountManager = DiscountManager.getInstance();
    }
    @Test
    public void getInstanceTest() {
        DiscountManager test = DiscountManager.getInstance();
        Assert.assertEquals(discountManager, test);
    }
}
