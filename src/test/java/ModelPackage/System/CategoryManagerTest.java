package ModelPackage.System;

import ModelPackage.Product.Category;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.category.RepeatedFeatureException;
import ModelPackage.System.exeption.category.RepeatedNameInParentCategoryExeption;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class CategoryManagerTest {
    private CategoryManager categoryManager;
    private Category main;
    private Category category1;
    private Category category2;
    private Category category3;

    {
        categoryManager = CategoryManager.getInstance();
        main = new Category("Main",null);
        main.setId("MNCTCFKala");
        category1 = new Category("cloth",main.getId());
        categoryManager.addToBase(category1,main);
        category2 = new Category("mobile",main.getId());
        categoryManager.addToBase(category2,main);
        category3 = new Category("men",category1.getId());
        categoryManager.addToBase(category3,category1);

        ArrayList<String> features = new ArrayList<>();
        features.add("art");
        features.add("size");
        features.add("color");
        category1.setSpecialFeatures(features);

        ArrayList<String> productCat1 = new ArrayList<>();
        productCat1.add("654");
        category1.setAllProductInThis(productCat1);

        ArrayList<String> productCat2 = new ArrayList<>();
        productCat2.add("123");
        category2.setAllProductInThis(productCat2);

        categoryManager.clear();
        categoryManager.add(main);
        categoryManager.add(category1);
        categoryManager.add(category2);
        categoryManager.addM(category1);
        categoryManager.addM(category2);
        categoryManager.add(category3);
    }

    @Test
    public void getInstanceTest(){
        CategoryManager temp = CategoryManager.getInstance();
        Assert.assertEquals(categoryManager,temp);
    }

    @Test
    public void createCategoryTest()
            throws RepeatedNameInParentCategoryExeption, NoSuchACategoryException {
        categoryManager.createCategory("women",category1.getId());
        int countOfSubCats = category1.getSubCategories().size();
        Assert.assertEquals(2,countOfSubCats);
    }

    @Test
    public void createCategoryRepNameExcTest(){
        Assert.assertThrows(RepeatedNameInParentCategoryExeption.class,() -> {
            categoryManager.createCategory("men",category1.getId());
        });
    }

    @Test
    public void createCategoryNoCatExcTest(){
        Assert.assertThrows(NoSuchACategoryException.class,() -> {
            categoryManager.createCategory("men","asdf");
        });
    }

    @Test
    public void addProductToCategoryTest(@Mocked ProductManager productManager)
            throws Exception{
       new Expectations(){{
           productManager.checkIfThisProductExists("abc");
           times = 1;
       }};
        categoryManager.addProductToCategory("abc",category1.getId());

        int countOfProducts = category1.getAllProductInThis().size();
        Assert.assertEquals(1,countOfProducts);
    }

    @Test(expected = NoSuchACategoryException.class)
    public void addProductToCategoryNoCatExTest() throws Exception{
        categoryManager.addProductToCategory("abc","bullshit");
    }

    @Test(expected = NoSuchAProductException.class)
    public void addProductToCategoryNoPrExTest() throws Exception {
        categoryManager.addProductToCategory("abc",category1.getId());
    }

    @Test
    public void getAllSpecialFeaturesFromCategoryTest() throws Exception{
        ArrayList<String> expected = new ArrayList<>();
        expected.add("art");
        expected.add("size");
        expected.add("color");

        ArrayList<String> actual = categoryManager.getAllSpecialFeaturesFromCategory(category1.getId());
        Assert.assertEquals(expected,actual);
    }

    @Test(expected = NoSuchACategoryException.class)
    public void getAllSpecialFeaturesFromCategoryNoCatExcTest() throws Exception{
        categoryManager.getAllSpecialFeaturesFromCategory("bullshit");
    }

    @Test
    public void editProductCategoryTest(@Mocked ProductManager productManager) throws Exception{
        new Expectations(){{
            productManager.checkIfThisProductExists("123");
            times = 1;
        }};
        categoryManager.editProductCategory("123",category2.getId(),category1.getId());

        int actualCountOfProductInCat1 = category1.getAllProductInThis().size();
        Assert.assertEquals(2,actualCountOfProductInCat1);
    }

    @Test(expected = NoSuchAProductInCategoryException.class)
    public void editProductCategoryNoPrInCtExcTest() throws Exception{
        categoryManager.editProductCategory("456",category2.getId(),category1.getId());
    }

    @Test
    public void removeProductFromCategoryTest() throws Exception {
        categoryManager.removeProductFromCategory("123",category2.getId());
        int countCat2Prs = category2.getAllProductInThis().size();
        Assert.assertEquals(0,countCat2Prs);
    }

    @Test
    public void editNameTest() throws Exception{
        categoryManager.editName("women",category1.getId());
        String actulaName = category1.getName();
        Assert.assertEquals("women",actulaName);
    }

    @Test(expected = RepeatedNameInParentCategoryExeption.class)
    public void editNameRenExcTest() throws Exception {
        categoryManager.editName("mobile",category1.getId());
    }

    @Test
    public void moveCategoryToAnotherParentTest() throws Exception{
        categoryManager.moveCategoryToAnotherParent("MNCTCFKala",category3.getId());
        int countOfMainCats = main.getSubCategories().size();
        Assert.assertEquals(3,countOfMainCats);

        int countOfCat1Cats = category1.getSubCategories().size();
        Assert.assertEquals(0,countOfCat1Cats);
    }

    @Test(expected = RepeatedNameInParentCategoryExeption.class)
    public void moveCategoryToAnotherParentRenExcTest() throws Exception{
       category3.setName("cloth");
       categoryManager.moveCategoryToAnotherParent("MNCTCFKala",category3.getId());
    }

    @Test
    public void checkIfThisFeatureExistInThisCategoryTest() throws Exception{
        new MockUp<CategoryManager>(){
            @Mock void addNewFeatureToProducts(String newFeature,ArrayList<String> products){}
        };

        categoryManager.addFeatureToCategory(category1.getId(),"AllO");
    }

    @Test(expected = RepeatedFeatureException.class)
    public void checkIfThisFeatureExistInThisCategoryReFeatureExcTest() throws Exception{
        categoryManager.addFeatureToCategory(category1.getId(),"art");
    }

}