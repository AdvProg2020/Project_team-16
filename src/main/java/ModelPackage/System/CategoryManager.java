package ModelPackage.System;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.category.*;
import ModelPackage.System.exeption.product.NoSuchAProductException;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryManager {
    private static CategoryManager categoryManager = new CategoryManager();
    private ArrayList<Category> allMainCategories;
    private ArrayList<Category> allCategories;

    private CategoryManager(){
        allMainCategories  = new ArrayList<>();
        allCategories = new ArrayList<>();
    }

    public static CategoryManager getInstance(){
        return categoryManager;
    }

    public Category getCategoryById(String categoryId)
            throws NoSuchACategoryException {
        for (Category category : allCategories) {
            if (category.getId().equals(categoryId)) return category;
        }
        throw new NoSuchACategoryException(categoryId);
    }

    public void createCategory(String name,String parentId)
            throws RepeatedNameInParentCategoryExeption,NoSuchACategoryException{
        Category parent = getCategoryById(parentId);
        checkIfThisNameIsValidForThisParent(name,parent);

        Category toCreate = new Category(name,parentId);
        addToBase(toCreate,parent);
        if (parentId.equals("MNCTCFKala")) allMainCategories.add(toCreate);
        allCategories.add(toCreate);
    }

    private void checkIfThisNameIsValidForThisParent(String name,Category parent)
            throws RepeatedNameInParentCategoryExeption {
        ArrayList<Category> subCategories = parent.getSubCategories();
        for (Category category : subCategories) {
            if (category.getName().equals(name))
                throw new RepeatedNameInParentCategoryExeption(name);
        }
    }

    public void addProductToCategory(String productId,String categoryId)
            throws NoSuchACategoryException, NoSuchAProductException {
        Category toBeAddedTo = getCategoryById(categoryId);
        ProductManager.getInstance().checkIfThisProductExists(productId);

        ArrayList<String> productsIn = toBeAddedTo.getAllProductInThis();
        productsIn.add(productId);
        toBeAddedTo.setAllProductInThis(productsIn);
    }

    public ArrayList<String> getAllSpecialFeaturesFromCategory(String categoryId)
            throws NoSuchACategoryException{
        Category category = getCategoryById(categoryId);
        return getAllSpecialFeatures(category);
    }

    private ArrayList<String> getAllSpecialFeatures(Category category)
            throws NoSuchACategoryException {
        ArrayList<String> features = new ArrayList<>();
        String parentId = category.getParentId();
        if (!parentId.equals("MNCTCFKala")){
            Category parent = getCategoryById(parentId);
            features = new ArrayList<>(getAllSpecialFeatures(parent));
        }
        features.addAll(category.getSpecialFeatures());
        return features;
    }

    public void editProductCategory(String productId,String oldCategoryId, String newCategoryId)
            throws NoSuchACategoryException, NoSuchAProductException, NoSuchAProductInCategoryException {
        Category oldCategory = getCategoryById(oldCategoryId);

        ProductManager.getInstance().checkIfThisProductExists(productId);
        checkIfThisProductExistInThisCategory(productId,oldCategory);

        ArrayList<String> products = oldCategory.getAllProductInThis();
        products.remove(productId);
        oldCategory.setAllProductInThis(products);

        Category newCategory = getCategoryById(newCategoryId);

        products = newCategory.getAllProductInThis();
        products.add(productId);
        newCategory.setAllProductInThis(products);

        setNewFeaturesToProduct(productId,newCategory.getSpecialFeatures());
    }

    private void setNewFeaturesToProduct(String productId,ArrayList<String> newFeatures){
        HashMap<String,String> features = new HashMap<>();
        for (String feature : newFeatures) {
            features.put(feature,"");
        }
        ProductManager.getInstance().findProductById(productId).setSpecialFeatures(features);
    }

    private void checkIfThisProductExistInThisCategory(String productId,Category category)
            throws NoSuchAProductInCategoryException {
        for (String id : category.getAllProductInThis()) {
            if (id.equals(productId))return;
        }
        throw new NoSuchAProductInCategoryException(productId,category.getId());
    }

    public void removeProductFromCategory(String productId,String categoryId)
            throws NoSuchACategoryException, NoSuchAProductInCategoryException {
        Category category = getCategoryById(categoryId);
        checkIfThisProductExistInThisCategory(productId,category);
        ArrayList<String> products = category.getAllProductInThis();
        products.remove(productId);
        category.setAllProductInThis(products);
    }

    public void editName(String name,String categoryId) throws
            NoSuchACategoryException, RepeatedNameInParentCategoryExeption {
        Category category = getCategoryById(categoryId);
        Category parent = getCategoryById(category.getParentId());
        checkIfThisNameIsValidForThisParent(name,parent);
        category.setName(name);
    }

    public void moveCategoryToAnotherParent(String newParentId,String categoryId)
            throws NoSuchACategoryException, RepeatedNameInParentCategoryExeption {
        Category category = getCategoryById(categoryId);
        Category currentParent = getCategoryById(category.getParentId());
        Category  newParent = getCategoryById(newParentId);
        checkIfThisNameIsValidForThisParent(category.getName(),newParent);
        category.setParentId(newParent.getId());

        ArrayList<Category> subcategories = newParent.getSubCategories();
        subcategories.add(category);
        newParent.setSubCategories(subcategories);

        subcategories = currentParent.getSubCategories();
        subcategories.remove(category);
        currentParent.setSubCategories(subcategories);
    }

    public void addFeatureToCategory(String categoryId,String newFeature)
            throws NoSuchACategoryException, RepeatedFeatureException {
        Category category = getCategoryById(categoryId);
        checkIfThisFeatureExistInThisCategory(category,newFeature);
        ArrayList<String> features = category.getSpecialFeatures();
        features.add(newFeature);
        category.setSpecialFeatures(features);
        addNewFeatureToProducts(newFeature,category.getAllProductInThis());
    }

    private void checkIfThisFeatureExistInThisCategory(Category category, String feature)
            throws RepeatedFeatureException {
        for (String specialFeature : category.getSpecialFeatures()) {
            if (specialFeature.equals(feature)) throw new RepeatedFeatureException(feature,category.getId());
        }
    }

    void addNewFeatureToProducts(String newFeature,ArrayList<String> products){
        for (String productId : products) {
            Product product = ProductManager.getInstance().findProductById(productId);
            HashMap<String,String> specialFeatures = product.getSpecialFeatures();
            specialFeatures.put(newFeature,"");
            product.setSpecialFeatures(specialFeatures);
            /* TODO : Notify The Seller To Add New Feature */
        }
    }

    public void removeCategory(String categoryId)
            throws NoSuchACategoryException {
        Category category = getCategoryById(categoryId);
        removeCategory(category);
    }

    private void removeCategory(Category category)
            throws NoSuchACategoryException {
        ArrayList<Category> subCategories = category.getSubCategories();
        if (!subCategories.isEmpty()){
            for (Category subCategory : subCategories) {
                removeCategory(subCategory);
            }
        }
        removeAllProductsIn(category);
        allCategories.remove(category);
        Category parent = getCategoryById(category.getId());
        ArrayList<Category> subcategories = parent.getSubCategories();
        subCategories.remove(category);
        parent.setSubCategories(subCategories);
    }

    private void removeAllProductsIn(Category category){
        for (String product : category.getAllProductInThis()) {
            ProductManager.getInstance().deleteProductCategoryOrder(product);
        }
    }

    public ArrayList<String> getAllProductsInThisCategory(String categoryId)
            throws NoSuchACategoryException {
        Category category = getCategoryById(categoryId);
        return getAllProductsInThisCategory(category);
    }

    private ArrayList<String> getAllProductsInThisCategory(Category category){
        ArrayList<String> products = new ArrayList<>(category.getAllProductInThis());
        ArrayList<Category> subcategories = category.getSubCategories();
        if (!subcategories.isEmpty()){
            for (Category subcategory : subcategories) {
                products.addAll(getAllProductsInThisCategory(subcategory));
            }
        }
        return products;
    }

    public void clear(){
        allCategories.clear();
        allMainCategories.clear();
    }

    public void add(Category cat){
        allCategories.add(cat);
    }

    public void addM(Category cat){
        allMainCategories.add(cat);
    }

    public void addToBase(Category cat,Category parent){
        ArrayList<Category> subCategories = parent.getSubCategories();
        subCategories.add(cat);
        parent.setSubCategories(subCategories);
    }

}
