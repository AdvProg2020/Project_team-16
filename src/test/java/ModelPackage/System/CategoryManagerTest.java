package ModelPackage.System;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.category.RepeatedFeatureException;
import ModelPackage.System.exeption.category.RepeatedNameInParentCategoryException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class CategoryManagerTest {
    private CategoryManager categoryManager;
    private Category main;
    private Category category1;
    private Category category2;
    private Category category3;
    private ArrayList<String> features;
    private Product product;

    {
        categoryManager = CategoryManager.getInstance();
        main = new Category("Main",null);
        main.setCategoryId("MNCTCFKala");
        main.setId(3);
        category1 = new Category("cloth",main);
        category1.setId(1);
        //categoryManager.addToBase(category1,main);

        /*main.getSubCategories().add(category1);
        category1.setParent(main);*/

        category2 = new Category("mobile",main);
        category2.setId(2);
        main.getSubCategories().add(category1);
        main.getSubCategories().add(category2);
        //categoryManager.addToBase(category2,main);
        category3 = new Category("men",category1);
        //categoryManager.addToBase(category3,category1);

        features = new ArrayList<>();
        features.add("art");
        features.add("size");
        features.add("color");
        category1.setSpecialFeatures(features);

        ArrayList<Product> productCat1 = new ArrayList<>();
        product = new Product();
        product.setId(1);
        product.setSpecialFeatures(new HashMap<>());

        FakeDBManager fakeDBManager = new FakeDBManager();
        fakeDBManager.save(product);
        fakeDBManager.save(category1);

        productCat1.add(product);
        category1.setAllProducts(productCat1);

        ArrayList<Product> productCat2 = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(2);
        category2.setAllProducts(productCat2);

        category3.setAllProducts(new ArrayList<>());
        category3.setSpecialFeatures(new ArrayList<>());
        category3.setId(10);


       /* categoryManager.clear();

        categoryManager.add(main);
        categoryManager.add(category1);
        categoryManager.add(category2);
        categoryManager.addM(category1);
        categoryManager.addM(category2);
        categoryManager.add(category3);*/
    }

    @Before
    public void initialize() {
        new MockUp<DBManager>() {
            @Mock
            public void save(Object object){}
            @Mock
            public <T>T load(Class<T> type, Serializable id) {
                if (type.isInstance(category2)){
                    if (id.equals(1))
                        return type.cast(category1);
                    else if (id.equals(2))
                        return type.cast(category2);
                    else if (id.equals(3))
                        return type.cast(main);
                    else if (id.equals(10))
                        return type.cast(category3);
                    return null;
                }
                else if (type.isInstance(product)) {
                    if (id.equals(1))
                        return type.cast(product);
                    return null;
                }
                return null;
            }
        };
        new MockUp<CategoryManager>(){
            @Mock
            public List<Category> getBaseCats() {
                List<Category> baseCategories = new ArrayList<>();
                baseCategories.add(main);
                return baseCategories;
            }
        };
    }

    @Test
    public void getInstanceTest(){
        CategoryManager temp = CategoryManager.getInstance();
        assertEquals(categoryManager,temp);
    }

    @Test
    public void createCategoryTest()
            throws RepeatedNameInParentCategoryException, NoSuchACategoryException {
        ArrayList<String> features = new ArrayList<>();
        features.add("art");
        features.add("size");
        features.add("color");
        categoryManager.createCategory("women",0, features);
    }

    @Test
    public void createCategoryRepNameExcTest(){
        Assert.assertThrows(RepeatedNameInParentCategoryException.class, () -> {
            categoryManager.createCategory(main.getName(),0, new ArrayList<>());
        });
    }

    @Test
    public void createCategoryNoSuchCategoryTest() {
        assertThrows(NoSuchACategoryException.class, () ->
                categoryManager.createCategory("sa", 4, new ArrayList<>()));
    }

    @Test
    public void addProductToCategoryTest()
            throws Exception{
       categoryManager.createCategory("cloth", 3, new ArrayList<>());
       categoryManager.addProductToCategory(new Product(1), category1);

        int countOfProducts = category1.getAllProducts().size();
        assertEquals(2,countOfProducts);
    }

    @Test
    public void getCategorySpecialFeaturesTest() throws NoSuchACategoryException {
        assertArrayEquals(categoryManager.getAllSpecialFeaturesFromCategory(category1.getId()).toArray(),
                features.toArray());
    }

    @Test
    public void getAllSpecialFeaturesTest() throws Exception{
        Assert.assertEquals(categoryManager.getAllSpecialFeaturesFromCategory(category3.getId()),
                new ArrayList<>());
    }

    @Test(expected = NoSuchACategoryException.class)
    public void getAllSpecialFeaturesFromCategoryNoCatExcTest() throws Exception{
        categoryManager.getAllSpecialFeaturesFromCategory(4);
    }

    @Test
    public void editProductCategoryTest(@Mocked ProductManager productManager) throws Exception{
        new Expectations(){{
            productManager.checkIfThisProductExists(1);
            times = 1;
        }};

        categoryManager.editProductCategory(1,category1.getId(),category2.getId());

        int actualCountOfProductInCat1 = category2.getAllProducts().size();
        assertEquals(1, actualCountOfProductInCat1);
    }

    @Test(expected = NoSuchAProductInCategoryException.class)
    public void editProductCategoryNoPrInCtExcTest() throws Exception{
        categoryManager.editProductCategory(1,category2.getId(),category1.getId());
    }

    @Test
    public void removeProductFromCategoryTest(@Mocked ProductManager productManager) throws Exception {
        Product product = new Product(1);
        new Expectations(){{
            productManager.findProductById(1);
            result = product;
        }};
        categoryManager.addProductToCategory(product, category1);
        categoryManager.removeProductFromCategory(1, 0);
        int countCat1Prs = category1.getAllProducts().size();
        assertEquals(1, countCat1Prs);
    }

    @Test
    public void editNameTest() throws Exception{
        categoryManager.editName("cloth", category1.getId());
        String actualName = category1.getName();
        Assert.assertEquals("cloth",actualName);
    }

    @Test(expected = RepeatedNameInParentCategoryException.class)
    public void editNameRenExcTest() throws Exception {
        categoryManager.editName(category2.getName(),category1.getId());
    }

    @Test
    public void moveCategoryToAnotherParentTest() throws Exception{
        categoryManager.moveCategoryToAnotherParent(3, category3.getId());
        int countOfMainCats = main.getSubCategories().size();
        assertEquals(3,countOfMainCats);

        int countOfCat1Cats = category1.getSubCategories().size();
        assertEquals(0,countOfCat1Cats);
    }

    @Test(expected = RepeatedNameInParentCategoryException.class)
    public void moveCategoryToAnotherParentRenExcTest() throws Exception{
       category3.setName(category2.getName());
       categoryManager.moveCategoryToAnotherParent(3,category3.getId());
    }

    @Test
    public void checkIfThisFeatureExistInThisCategoryTest() throws Exception{
        new MockUp<CategoryManager>(){
            @Mock void addNewFeatureToProducts(String newFeature,ArrayList<String> products){}
        };

        categoryManager.addFeatureToCategory(category1,"AllO");
    }

    @Test(expected = RepeatedFeatureException.class)
    public void checkIfThisFeatureExistInThisCategoryReFeatureExcTest() throws Exception{
        categoryManager.addFeatureToCategory(category1,"art");
    }

    /*@Test(expected = NoSuchACategoryException.class)
    public void removeCategory() throws Exception{
        new MockUp<ProductManager>(){
          @Mock public void deleteProductCategoryOrder(int productId){};
        };
        categoryManager.removeCategory(category1.getCategoryId());
        int countOfMainSubCats = main.getSubCategories().size();
        Assert.assertEquals(1,countOfMainSubCats);

        categoryManager.getCategoryById(category1.getCategoryId());
    }

    @Test
    public void getAllProductsInThisCategoryTest() throws Exception{
        ArrayList<String> actulaProducts = categoryManager.getAllProductsInThisCategory(main.getCategoryId());
        ArrayList<String> expectedProducts = new ArrayList<>();
        expectedProducts.add("654");
        expectedProducts.add("123");
        Assert.assertEquals(expectedProducts,actulaProducts);
    }*/

}