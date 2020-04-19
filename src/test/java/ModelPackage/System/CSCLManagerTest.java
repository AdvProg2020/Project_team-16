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
                            /*both states of find company by name*/
    @Test
    public void getCompanyByNameTest() {
        String name = "Adidas";
        Company actualCompany = csclManager.getCompanyByName(name);
        Company expectedCompany = adidas;
        Assert.assertEquals(actualCompany, expectedCompany);
    }
    @Test
    public void  getCompanyByNameNotFoundTest() {
        String name = "Pishgaman";
        Company actualCompany = csclManager.getCompanyByName(name);
        Assert.assertNull(actualCompany);
    }
}
