package ModelPackage.System;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.category.*;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.PrintModels.CategoryPM;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CategoryManager {
    private static CategoryManager categoryManager = new CategoryManager();
    private static ArrayList<String> publicFeatures;

    private CategoryManager(){

    }

    public static CategoryManager getInstance(){
        return categoryManager;
    }

    public Category getCategoryById(int categoryId)
            throws NoSuchACategoryException {
        Category category = DBManager.load(Category.class,categoryId);
        if (category != null) return category;
        else throw new NoSuchACategoryException(Integer.toString(categoryId));
    }

    public void createCategory(String name,int parentId)
            throws RepeatedNameInParentCategoryExeption,NoSuchACategoryException{
        Category parent = getCategoryById(parentId);
        checkIfThisNameIsValidForThisParent(name,parent);

        Category toCreate = new Category(name,parent);
        addToBase(toCreate,parent);
        DBManager.save(toCreate);
    }

    private void checkIfThisNameIsValidForThisParent(String name,Category parent)
            throws RepeatedNameInParentCategoryExeption {
        List<Category> subCategories = parent.getSubCategories();
        for (Category category : subCategories) {
            if (category.getName().equals(name))
                throw new RepeatedNameInParentCategoryExeption(name);
        }
    }

    public void addProductToCategory(int productId,int categoryId)
            throws NoSuchACategoryException, NoSuchAProductException {
        Category toBeAddedTo = getCategoryById(categoryId);
        ProductManager.getInstance().checkIfThisProductExists(productId);

        List<Product> productsIn = toBeAddedTo.getAllProducts();
        productsIn.add(ProductManager.getInstance().findProductById(productId));
        toBeAddedTo.setAllProducts(productsIn);
        DBManager.save(toBeAddedTo);
    }

    public ArrayList<String> getAllSpecialFeaturesFromCategory(int categoryId)
            throws NoSuchACategoryException{
        Category category = getCategoryById(categoryId);
        return getAllSpecialFeatures(category);
    }

    private ArrayList<String> getAllSpecialFeatures(Category category) {
        ArrayList<String> features = new ArrayList<>();
        if (category.getParent() != null){
            Category parent = category.getParent();
            features = new ArrayList<>(getAllSpecialFeatures(parent));
        }
        features.addAll(category.getSpecialFeatures());
        return features;
    }

    public void editProductCategory(int productId,int oldCategoryId,int newCategoryId)
            throws NoSuchACategoryException, NoSuchAProductException, NoSuchAProductInCategoryException {
        Category oldCategory = getCategoryById(oldCategoryId);
        ProductManager.getInstance().checkIfThisProductExists(productId);
        checkIfThisProductExistInThisCategory(productId,oldCategory);
        Product product = ProductManager.getInstance().findProductById(productId);
        List<Product> products = oldCategory.getAllProducts();
        products.remove(product);
        oldCategory.setAllProducts(products);
        Category newCategory = getCategoryById(newCategoryId);
        products = newCategory.getAllProducts();
        products.add(product);
        newCategory.setAllProducts(products);
        product.setCategory(newCategory);
        setNewFeaturesToProduct(product,newCategory.getSpecialFeatures());
        DBManager.save(newCategory);
        DBManager.save(oldCategory);
        DBManager.save(product);
    }

    private void setNewFeaturesToProduct(Product product,List<String> newFeatures) {
        HashMap<String,String> features = new HashMap<>();
        for (String feature : newFeatures) {
            features.put(feature,"");
        }
        product.setSpecialFeatures(features);
        /* TODO : send message to seller to edit the product's feature */
    }

    private void checkIfThisProductExistInThisCategory(int productId,Category category)
            throws NoSuchAProductInCategoryException {
        for (Product product : category.getAllProducts()) {
            if (product.getId() == productId)return;
        }
        throw new NoSuchAProductInCategoryException(productId,Integer.toString(category.getId()));
    }

    public void removeProductFromCategory(int productId,int categoryId)
            throws NoSuchACategoryException, NoSuchAProductInCategoryException, NoSuchAProductException {
        Category category = getCategoryById(categoryId);
        Product product = ProductManager.getInstance().findProductById(productId);
        checkIfThisProductExistInThisCategory(productId,category);
        List<Product> products = category.getAllProducts();
        products.remove(product);
        category.setAllProducts(products);
        DBManager.save(product);
        DBManager.save(category);
    }

    public void editName(String name,int categoryId) throws
            NoSuchACategoryException, RepeatedNameInParentCategoryExeption {
        Category category = getCategoryById(categoryId);
        Category parent = category.getParent();
        checkIfThisNameIsValidForThisParent(name,parent);
        category.setName(name);
        DBManager.save(category);
    }

    public void moveCategoryToAnotherParent(int newParentId,int categoryId)
            throws NoSuchACategoryException, RepeatedNameInParentCategoryExeption {
        Category category = getCategoryById(categoryId);
        Category currentParent = category.getParent();
        Category  newParent = getCategoryById(newParentId);
        checkIfThisNameIsValidForThisParent(category.getName(),newParent);
        category.setParent(newParent);

        List<Category> subcategories = newParent.getSubCategories();
        subcategories.add(category);
        newParent.setSubCategories(subcategories);

        subcategories = currentParent.getSubCategories();
        subcategories.remove(category);
        currentParent.setSubCategories(subcategories);
        DBManager.save(currentParent);
        DBManager.save(newParent);
        DBManager.save(category);
    }

    public void addFeatureToCategory(int categoryId,String newFeature)
            throws NoSuchACategoryException, RepeatedFeatureException {
        Category category = getCategoryById(categoryId);
        checkIfThisFeatureExistInThisCategory(category,newFeature);
        List<String> features = category.getSpecialFeatures();
        features.add(newFeature);
        category.setSpecialFeatures(features);
        addNewFeatureToProducts(newFeature,category.getAllProducts());
        DBManager.save(category);
    }

    private void checkIfThisFeatureExistInThisCategory(Category category, String feature)
            throws RepeatedFeatureException {
        for (String specialFeature : category.getSpecialFeatures()) {
            if (specialFeature.equals(feature)) throw new RepeatedFeatureException(feature,Integer.toString(category.getId()));
        }
    }

    void addNewFeatureToProducts(String newFeature,List<Product> products){
        for (Product product : products) {
            Map<String,String> specialFeatures = product.getSpecialFeatures();
            specialFeatures.put(newFeature,"");
            product.setSpecialFeatures(specialFeatures);
            /* TODO : Notify The Seller To Add New Feature */
        }
    }

    public void removeCategory(int categoryId)
            throws NoSuchACategoryException {
        Category category = getCategoryById(categoryId);
        removeCategory(category);
    }

    private void removeCategory(Category category) {
        List<Category> subCategories = category.getSubCategories();
        if (!subCategories.isEmpty()){
            for (Category subCategory : subCategories) {
                removeCategory(subCategory);
            }
        }
        removeAllProductsIn(category);
        Category parent = category.getParent();
        List<Category> subcategories = parent.getSubCategories();
        subCategories.remove(category);
        parent.setSubCategories(subcategories);
        DBManager.delete(category);
        DBManager.save(parent);
    }

    private void removeAllProductsIn(Category category){
        ProductManager productManager = ProductManager.getInstance();
        for (Product product : category.getAllProducts()) {
            productManager.deleteProductCategoryOrder(product);
        }
    }

    public ArrayList<Product> getAllProductsInThisCategory(int categoryId)
            throws NoSuchACategoryException {
        Category category = getCategoryById(categoryId);
        return (ArrayList<Product>) getAllProductsInThisCategory(category);
    }

    private List<Product> getAllProductsInThisCategory(Category category){
        List<Product> products = category.getAllProducts();
        List<Category> subcategories = category.getSubCategories();
        if (!subcategories.isEmpty()){
            for (Category subcategory : subcategories) {
                products.addAll(getAllProductsInThisCategory(subcategory));
            }
        }
        return products;
    }


    /* TODO : Get Categories in Another Category */

    public static ArrayList<String> getPublicFeatures() {
        return publicFeatures;
    }

    public void addToBase(Category cat,Category parent){
        List<Category> subCategories = parent.getSubCategories();
        subCategories.add(cat);
        parent.setSubCategories(subCategories);
    }

}
