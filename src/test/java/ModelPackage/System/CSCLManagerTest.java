package ModelPackage.System;

import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Company;
import org.junit.Assert;
import org.junit.Test;

public class CSCLManagerTest {
    private CSCLManager csclManager;
    private Company adidas;
    private Company puma;
    private String[] data = {"12", "ali12", "solution", "solved correctly"};
    {
        csclManager = CSCLManager.getInstance();
        adidas = new Company("Adidas", "34524532", "Sports");
        puma = new Company("Puma", "12434565", "Sports");
        csclManager.createCompany(adidas);
        csclManager.createCompany(puma);
    }
    @Test
    public void getInstanceTest() {
        CSCLManager test = CSCLManager.getInstance();
        Assert.assertEquals(test, csclManager);
    }
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
    @Test
    public void editCompanyNameTest() {
        String expectedName = "Pishgaman";
        csclManager.editCompanyName("Adidas", expectedName);
        String actualName = adidas.getName();
        Assert.assertEquals(actualName, expectedName);
    }
    @Test
    public void editCompanyGroupTest() {
        String expectedGroup = "SuperSport";
        csclManager.editCompanyGroup("Puma", expectedGroup);
        String actualGroup = puma.getGroup();
        Assert.assertEquals(actualGroup, expectedGroup);
    }
    @Test
    public void doesCompanyExistTest() {
        boolean actual = csclManager.doesCompanyExist("Adidas");
        Assert.assertTrue(actual);
        actual = csclManager.doesCompanyExist("pishgaman");
        Assert.assertFalse(actual);
    }
}
