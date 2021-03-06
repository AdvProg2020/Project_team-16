package ModelPackage.System;

import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.Product.ProductStatus;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.System.editPackage.CategoryEditAttribute;
import ModelPackage.System.exeption.category.*;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.PrintModels.MicroProduct;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class CategoryManager {
    private static CategoryManager categoryManager = new CategoryManager();
    private static final ArrayList<String> publicFeatures = new ArrayList<>(Arrays.asList("Dimension", "Weigh", "Color"));

    private CategoryManager() {

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

    public void createCategory(String name,int parentId,List<String> features)
            throws RepeatedNameInParentCategoryException, NoSuchACategoryException {
        Category parent;
        if (parentId != 0)
            parent = getCategoryById(parentId);
        else
            parent = null;
        checkIfThisNameIsValidForThisParent(name,parent);

        Category toCreate = new Category(name,parent);
        toCreate.setSpecialFeatures(features);
        addToBase(toCreate,parent);
        DBManager.save(toCreate);
    }

    private void checkIfThisNameIsValidForThisParent(String name,Category parent)
            throws RepeatedNameInParentCategoryException {
        List<Category> subCategories;
        if (parent != null)
            subCategories = parent.getSubCategories();
        else
            subCategories = getBaseCats();
        for (Category category : subCategories) {
            if (category.getName().equals(name))
                throw new RepeatedNameInParentCategoryException(name);
        }
    }

    public List<Category> getBaseCats() {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
        Root<Category> root = criteriaQuery.from(Category.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                criteriaBuilder.isNull(root.get("parent").as(Category.class))
        );
        Query<Category> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public ArrayList<MicroProduct> allProductsInACategoryList(int id) throws NoSuchACategoryException {
        ArrayList<MicroProduct> list = new ArrayList<>();
        Category category = getCategoryById(id);
        for (Product product : category.getAllProducts()) {
            if (product.getProductStatus().equals(ProductStatus.VERIFIED))
                list.add(new MicroProduct(product.getName(), product.getId()));
        }
        return list;
    }

    public void addProductToCategory(Product product,Category toBeAddedTo) {
        List<Product> productsIn = toBeAddedTo.getAllProducts();
        productsIn.add(product);
        toBeAddedTo.setAllProducts(productsIn);
        DBManager.save(toBeAddedTo);
    }

    public ArrayList<String> getAllSpecialFeaturesFromCategory(int categoryId)
            throws NoSuchACategoryException{
        if (categoryId == 0) return new ArrayList<>();
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
        product.setProductStatus(ProductStatus.UNDER_EDIT);
    }

    private void checkIfThisProductExistInThisCategory(int productId,Category category)
            throws NoSuchAProductInCategoryException {
        for (Product product : category.getAllProducts()) {
            if (product.getId() == productId)return;
        }
        throw new NoSuchAProductInCategoryException(productId,Integer.toString(category.getId()));
    }

    void removeProductFromCategory(int productId,int categoryId)
            throws NoSuchACategoryException, NoSuchAProductInCategoryException, NoSuchAProductException {
        Category category = getCategoryById(categoryId);
        Product product = ProductManager.getInstance().findProductById(productId);
        checkIfThisProductExistInThisCategory(productId,category);
        List<Product> products = category.getAllProducts();
        products.remove(product);
        category.setAllProducts(products);
        DBManager.save(category);
    }

    public void editCategory(int categoryId, CategoryEditAttribute editAttribute)
            throws NoSuchACategoryException, RepeatedNameInParentCategoryException, RepeatedFeatureException, NoSuchAFeatureInCategoryException {
        Category category = getCategoryById(categoryId);

        String newName = editAttribute.getName();
        String addFeature = editAttribute.getAddFeature();
        String removeFeature = editAttribute.getRemoveFeature();
        int newParentId = editAttribute.getNewParentId();

        if (newName != null){
            editName(newName, categoryId);
        }
        if (addFeature != null){
            addFeatureToCategory(category, addFeature);
        }
        if (removeFeature != null){
            removeFeatureInCategory(category, removeFeature);
        }
        if (newParentId != 0) {
            CategoryManager.getInstance().moveCategoryToAnotherParent(newParentId,categoryId);
        }

        DBManager.save(category);
    }

    public void editName(String name,int categoryId) throws
            NoSuchACategoryException, RepeatedNameInParentCategoryException {
        Category category = getCategoryById(categoryId);
        Category parent = category.getParent();
        checkIfThisNameIsValidForThisParent(name,parent);
        category.setName(name);
        DBManager.save(category);
    }

    public void moveCategoryToAnotherParent(int newParentId,int categoryId)
            throws NoSuchACategoryException, RepeatedNameInParentCategoryException {
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

    public void addFeatureToCategory(Category category, String newFeature)
            throws RepeatedFeatureException {
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

    public void removeFeatureInCategory(Category category, String removeFeature) {
        List<String> features = category.getSpecialFeatures();
        features.remove(removeFeature);
        category.setSpecialFeatures(features);
        removeAFeatureFromProducts(removeFeature,category.getAllProducts());
        DBManager.save(category);
    }

    void addNewFeatureToProducts(String newFeature,List<Product> products){
        for (Product product : products) {
            Map<String,String> specialFeatures = product.getSpecialFeatures();
            specialFeatures.put(newFeature,"");
            product.setSpecialFeatures(specialFeatures);
            DBManager.save(product);
            MessageManager messageManager = MessageManager.getInstance();
            String name = product.getName();
            product.getPackages().forEach(sellPackage -> messageManager.sendMessage(sellPackage.getSeller(), "Feature Added",
                    String.format("Edit Your Product \"%s\", Some Features Added to Category", name)));
        }
    }

    void removeAFeatureFromProducts(String removeFeature, List<Product> products){
        for (Product product : products) {
            Map<String,String> specialFeatures = product.getSpecialFeatures();
            specialFeatures.remove(removeFeature);
            product.setSpecialFeatures(specialFeatures);
        }
    }

    public void removeCategory(int categoryId)
            throws NoSuchACategoryException {
        Category category = getCategoryById(categoryId);
        removeCategory(category);
    }

    private void removeCategory(Category category) {
        if (!category.getSubCategories().isEmpty())
            for (Category subCategory : category.getSubCategories()) {
                removeCategory(subCategory);
            }
        removeAllProductsIn(category);
        category.setAllProducts(null);
        Category parent = category.getParent();
        if (parent != null) {
            parent.getSubCategories().remove(category);
            DBManager.save(parent);
        }
        category.setParent(null);
        DBManager.delete(category);
    }

    private void removeAllProductsIn(Category category){
        ProductManager productManager = ProductManager.getInstance();
        List<Product> list = new CopyOnWriteArrayList<>(category.getAllProducts());
        for (Product product : list) {
            productManager.deleteProduct(product);
        }
    }

    List<Product> getAllProductsInThisCategory(int categoryId)
            throws NoSuchACategoryException {
        if (categoryId == 0) return ProductManager.getInstance().getAllProductsActive();
        Category category = getCategoryById(categoryId);
        return getAllProductsInThisCategory(category);
    }

    private List<Product> getAllProductsInThisCategory(Category category){
        List<Product> products = new ArrayList<>(category.getAllProducts());
        List<Category> subcategories = category.getSubCategories();
        if (!subcategories.isEmpty()){
            for (Category subcategory : subcategories) {
                products.addAll(getAllProductsInThisCategory(subcategory));
            }
        }
        return products;
    }

    public static ArrayList<String> getPublicFeatures() {
        return publicFeatures;
    }

    void addToBase(Category cat, Category parent) {
        List<Category> subCategories;
        if (parent != null)
            subCategories = parent.getSubCategories();
        else
            subCategories = getBaseCats();
        subCategories.add(cat);
        if (parent != null){
            parent.setSubCategories(subCategories);
            DBManager.save(parent);
        }
    }

}
