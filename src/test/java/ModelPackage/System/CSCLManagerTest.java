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
    private Comment comment;
                                /*create company*/
    {
        csclManager = CSCLManager.getInstance();
        adidas = new Company("Adidas", "34524532", "Sports");
        puma = new Company("Puma", "12434565", "Sports");
        csclManager.createCompany(adidas);
        csclManager.createCompany(puma);
        comment = new Comment("12", "ali12", "solution", "solved correctly", CommentStatus.VERIFIED, true);
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
                        /*edit name of company test*/
    @Test
    public void editCompanyNameTest() {
        String expectedName = "Pishgaman";
        csclManager.editCompanyName("Adidas", expectedName);
        String actualName = adidas.getName();
        Assert.assertEquals(actualName, expectedName);
    }
                        /*edit group of company test*/
    @Test
    public void editCompanyGroupTest() {
        String expectedGroup = "SuperSport";
        csclManager.editCompanyGroup("Puma", expectedGroup);
        String actualGroup = puma.getGroup();
        Assert.assertEquals(actualGroup, expectedGroup);
    }
}
