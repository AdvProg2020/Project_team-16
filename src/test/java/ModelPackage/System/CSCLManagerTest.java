package ModelPackage.System;

import ModelPackage.Product.Company;
import org.junit.Assert;
import org.junit.Test;

public class CSCLManagerTest {
    private CSCLManager csclManager;
    private Company adidas;
    private Company puma;
                                /*create company*/
    {
        csclManager = CSCLManager.getInstance();
        adidas = new Company("Adidas", "34524532", "Sports");
        puma = new Company("Puma", "12434565", "sports");
        csclManager.createCompany(adidas);
        csclManager.createCompany(puma);
    }
                                /*getInstance*/
    @Test
    public void getInstanceTest() {
        CSCLManager test = CSCLManager.getInstance();
        Assert.assertEquals(test, csclManager);
    }
}
