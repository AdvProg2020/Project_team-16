package ModelPackage.System;

import ModelPackage.Product.Product;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.filters.InvalidFilterException;

import java.util.*;

public class FilterManager {

    public static ArrayList<Product> updateFilterList(int categoryId, HashMap<String,String> filters,int[] priceRange)
            throws NoSuchACategoryException, InvalidFilterException {
        List<Product> allProductsInCategory = CategoryManager.getInstance().getAllProductsInThisCategory(categoryId);
        ArrayList<String>  validFeatures = CategoryManager.getInstance().getAllSpecialFeaturesFromCategory(categoryId);
        validFeatures.addAll(CategoryManager.getPublicFeatures());
        Set<String> filterSet = filters.keySet();
        ArrayList<String> filter = new ArrayList<>(filterSet);
        checkIfFiltersAreAvailable(filter,validFeatures);
        return matchProductsToFilters(allProductsInCategory,filters,priceRange);
    }

    public static List<Product> filterList(List<Product> list,HashMap<String,String> filters,int[] priceRange){
        List<Product> toReturn = new ArrayList<>();
        for (Product product : list) {
            if (doesMatchTheFilters(product,filters,priceRange))toReturn.add(product);
        }
        return toReturn;
    }

    private static boolean doesMatchTheFilters(Product product,HashMap<String,String> filters,int[] priceRange){
        if (!thisProductIsInPriceRange(priceRange[0],priceRange[1],product.getLeastPrice())) return false;
        HashMap<String,String> features = new HashMap<>(product.getPublicFeatures());
        features.putAll(product.getSpecialFeatures());
        for (String filter : filters.keySet()) {
            if (!features.containsKey(filter))return false;
            if (!features.get(filter).equals(filters.get(filter)))return false;
        }
        return true;
    }

    private static ArrayList<Product> matchProductsToFilters(List<Product> products,HashMap<String,String> filters,int[] priceRange){
        ArrayList<Product> filteredProducts = new ArrayList<>();
        ProductManager productManager = ProductManager.getInstance();
        for (Product product : products) {
            if (thisProductMatchesFilters(productManager.allFeaturesOf(product),filters)
                    && thisProductIsInPriceRange(priceRange[0],priceRange[1],product.getLeastPrice()))
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

    private static boolean thisProductIsInPriceRange(int lower, int high, int leastPrice){
        if (high == 0) return true;
        return (leastPrice >= lower && leastPrice <= high);
    }
}
