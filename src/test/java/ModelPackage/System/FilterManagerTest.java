package ModelPackage.System;

import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.filters.InvalidFilterException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterManagerTest {
    // TODO : edit tests
    @Test
    public void updateFilterListTest(@Mocked ProductManager productManager)
            throws NoSuchACategoryException, InvalidFilterException, NoSuchAProductException {
        Product product1 = new Product(1);
        Product product2 = new Product(2);

        new MockUp<CategoryManager>(){
            @Mock ArrayList<String> getAllProductsInThisCategory(String categoryId) throws NoSuchACategoryException{
                ArrayList<String> product = new ArrayList<>();
                product.add("1");
                product.add("2");
                return product;
            }

            @Mock  ArrayList<String> getAllSpecialFeaturesFromCategory(String categoryId)
                    throws NoSuchACategoryException {
                ArrayList<String> feature = new ArrayList<>();
                feature.add("Length");
                return feature;
            }

            @Mock  ArrayList<String> getPublicFeatures(){
                ArrayList<String> feature = new ArrayList<>();
                feature.add("Color");
                return feature;
            }
        };
        new MockUp<ProductManager>(){
            @Mock public HashMap<String,String> allFeaturesOf(Product product){
                HashMap<String,String> feature = new HashMap<>();
                feature.put("Color","Red");
                return feature;
            }
        };
        new Expectations(){{
           productManager.findProductById(1);
           result = product1;
           times = 1;

           productManager.findProductById(2);
           result = product2;
           times = 1;
        }};

        HashMap<String,String> filters = new HashMap<>();
        filters.put("Color","Red");
        product1.setLeastPrice(20);
        product2.setLeastPrice(40);

        int[] priceRange = {15,30};

        ArrayList<Product> productsFound = FilterManager.updateFilterList(12,filters,priceRange);
        ArrayList<Product> actual = new ArrayList<>();
        actual.add(product1);
        Assert.assertEquals(actual,productsFound);
    }

    @Test(expected = InvalidFilterException.class)
    public void updateFilterListInvalidFilterExc()
            throws NoSuchACategoryException, InvalidFilterException {
        new MockUp<CategoryManager>(){
           @Mock ArrayList<String> getAllProductsInThisCategory(String categoryId) throws NoSuchACategoryException{
               ArrayList<String> product = new ArrayList<>();
               product.add("1");
               product.add("2");
               return product;
            }

           @Mock  ArrayList<String> getAllSpecialFeaturesFromCategory(String categoryId)
                    throws NoSuchACategoryException {
                ArrayList<String> feature = new ArrayList<>();
                feature.add("Length");
                return feature;
           }

           @Mock  ArrayList<String> getPublicFeatures(){
               ArrayList<String> feature = new ArrayList<>();
               feature.add("Color");
               return feature;
           }
        };
        HashMap<String,String> filters = new HashMap<String, String>();
        filters.put("Loo","adsf");
        FilterManager.updateFilterList(1,filters,new int[2]);
    }
}
