package ModelPackage.System;

import org.junit.Assert;
import org.junit.Test;

public class CSCLManagerTest {
    private CSCLManager csclManager = CSCLManager.getInstance();
    @Test
    public void getInstanceTest() {
        CSCLManager test = CSCLManager.getInstance();
        Assert.assertEquals(test, csclManager);
    }
}
