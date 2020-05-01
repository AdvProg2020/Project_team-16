package ModelPackage.System;

import org.junit.Assert;
import org.junit.Test;

public class SortManagerTest {
    private SortManager sortManager;
    {
        sortManager = SortManager.getInstance();
    }
    @Test
    public void getInstanceTest() {
        SortManager test = SortManager.getInstance();
        Assert.assertEquals(sortManager, test);
    }
}
