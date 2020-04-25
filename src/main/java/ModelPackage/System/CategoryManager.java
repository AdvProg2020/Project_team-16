package ModelPackage.System;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.System.exeption.category.*;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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
        return category.getSpecialFeatures();
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
