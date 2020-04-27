package ModelPackage.System;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.filters.InvalidFilterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FilterManager {

    public static ArrayList<Product> updateFilterList(String categoryId, HashMap<String,String> filters)
            throws NoSuchACategoryException, InvalidFilterException {
        ArrayList<String> allProductsInCategory = CategoryManager.getInstance().getAllProductsInThisCategory(categoryId);
        ArrayList<String>  validFeatures = CategoryManager.getInstance().getAllSpecialFeaturesFromCategory(categoryId);
        validFeatures.addAll(CategoryManager.getPublicFeatures());
        Set<String> filterSet = filters.keySet();
        ArrayList<String> filter = new ArrayList<>(filterSet);
        checkIfFiltersAreAvailable(filter,validFeatures);
        return matchProductsToFilters(allProductsInCategory,filters);
    }

    private static ArrayList<Product> matchProductsToFilters(ArrayList<String> products,HashMap<String,String> filters){
        ArrayList<Product> filteredProducts = new ArrayList<>();
        ProductManager productManager = ProductManager.getInstance();
        for (String productId : products) {
            Product product = productManager.findProductById(productId);
            if (thisProductMatchesFilters(productManager.allFeaturesOf(product),filters))
                filteredProducts.add(product);
        }
        return filteredProducts;
    }

    private static boolean thisProductMatchesFilters(HashMap<String,String> features,HashMap<String,String> filters){
        for (String filter : filters.keySet()) {
            if (!features.get(filter).equals(filters.get(filter))){
                return false;
            }
        }
        return true;
    }

    private static void checkIfFiltersAreAvailable(ArrayList<String> filters,ArrayList<String> features)
        throws InvalidFilterException {
        for (String filter : filters) {
            if (!features.contains(filter)) throw new InvalidFilterException(filter);
        }
    }
}
