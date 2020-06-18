package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.Off.OffStatus;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.System.editPackage.ProductEditAttribute;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.off.NoSuchAOffException;
import ModelPackage.System.exeption.product.AlreadyASeller;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;
import ModelPackage.Users.Seller;
import com.google.gson.Gson;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestManager {
    private static RequestManager requestManager = null;

    private RequestManager() {
    }

    public void addRequest(Request request){
        DBManager.save(request);
    }

    public Request findRequestById(int id)
            throws NoSuchARequestException {
        Request request = DBManager.load(Request.class,id);
        if (request == null) throw new NoSuchARequestException(id);
        else return request;
    }

    public void accept(int requestId)
            throws NoSuchARequestException, NoSuchAProductException {
        Request request = findRequestById(requestId);
        RequestType type = request.getRequestType();
        switch (type){
            case CREATE_PRODUCT:
                acceptCreateProduct(request);
            case CHANGE_PRODUCT:
                acceptEditProduct(request);
                break;
            case CREATE_OFF:
                acceptCreateOff(request);
                break;
            case EDIT_OFF:
                acceptEditOff(request);
            case ASSIGN_COMMENT:
                acceptAssignComment(request);
                break;
            case REGISTER_SELLER:
                acceptSeller(request);
                break;
        }
        request.setDone(true);
        request.setAccepted(true);
        DBManager.save(request);
    }

    private void acceptCreateProduct(Request request){
        Product product = request.getProduct();
        product.setProductStatus(ProductStatus.VERIFIED);
        ProductManager.getInstance().addToActive(product);
        DBManager.save(product);
        CategoryManager.getInstance().addProductToCategory(product,product.getCategory());
        Seller seller = request.getSeller();
        SellPackage sellPackage = product.getPackages().get(0);
        seller.getPackages().add(sellPackage);
        DBManager.save(seller);
    }

    private void acceptEditProduct(Request request) throws NoSuchAProductException {
        ProductEditAttribute editAttribute = request.getProductEditAttribute();
        Product product = ProductManager.getInstance().findProductById(editAttribute.getSourceId());
        product.setProductStatus(ProductStatus.VERIFIED);
        if (editAttribute.getName() != null) {
            product.setName(editAttribute.getName());
            DBManager.save(product);
        }
        if (editAttribute.getPublicFeatureTitle() != null) {
            editPublicFeatureProduct(product,editAttribute);
        }
        if (editAttribute.getSpecialFeatureTitle() != null) {
            editSpecialFeatureProduct(product,editAttribute);
        }
        if (editAttribute.getNewCategoryId() != 0){
            try {
                CategoryManager.getInstance().editProductCategory(product.getId(),product.getCategory().getId(),editAttribute.getNewCategoryId());
            } catch (Exception e) {}
        }
        ProductManager.getInstance().addToActive(product);
        DBManager.delete(editAttribute);
    }

    private void editPublicFeatureProduct(Product product,ProductEditAttribute editAttribute){
        String title = editAttribute.getPublicFeatureTitle();
        String feature = editAttribute.getPublicFeature();
        Map<String, String> features = product.getPublicFeatures();
        if (features.containsKey(title)){
            features.replace(title,feature);
        }
        DBManager.save(product);
    }

    private void editSpecialFeatureProduct(Product product,ProductEditAttribute editAttribute){
        String title = editAttribute.getSpecialFeatureTitle();
        String feature = editAttribute.getSpecialFeature();
        Map<String, String> features = product.getSpecialFeatures();
        if (features.containsKey(title)){
            features.replace(title,feature);
        }
        DBManager.save(product);
    }

    private void acceptCreateOff(Request request){
        Off off = request.getOff();
        off.setOffStatus(OffStatus.ACCEPTED);
        DBManager.save(off);
        addOffToProducts(off);
        Seller seller = off.getSeller();
        List<Off> offs = seller.getOffs();
        offs.add(off);
        seller.setOffs(offs);
        DBManager.save(seller);
    }

    private void addOffToProducts(Off off){
        String seller = off.getSeller().getUsername();
        for (Product product : off.getProducts()) {
            try {
                SellPackage sellPackage = product.findPackageBySeller(seller);
                sellPackage.setOff(off);
                sellPackage.setOnOff(true);
                DBManager.save(sellPackage);
            } catch (NoSuchSellerException ignore) {}
        }
    }

    private void acceptEditOff(Request request){
        OffChangeAttributes changeAttributes = request.getOffEdit();
        try {
            Off off = OffManager.getInstance().findOffById(changeAttributes.getSourceId());
            if (changeAttributes.getStart() != null){
                off.setStartTime(changeAttributes.getStart());
            }
            if (changeAttributes.getEnd() != null){
                off.setEndTime(changeAttributes.getEnd());
            }
            if (changeAttributes.getPercentage() != 0){
                off.setOffPercentage(changeAttributes.getPercentage());
            }
            if (changeAttributes.getProductIdToAdd() != 0){
                addProductToOff(off,changeAttributes);
            }
            if (changeAttributes.getProductIdToRemove() != 0){
                removeProductToOff(off,changeAttributes);
            }
            DBManager.save(off);
        } catch (NoSuchAOffException e) {
        } finally {
            DBManager.delete(changeAttributes);
            DBManager.delete(request);
        }
    }

    private void addProductToOff(Off off,OffChangeAttributes changeAttributes){
        try {
            Product product = ProductManager.getInstance().findProductById(changeAttributes.getProductIdToAdd());
            off.getProducts().add(product);
            DBManager.save(product);
        } catch (NoSuchAProductException e) {

        }
    }

    private void removeProductToOff(Off off,OffChangeAttributes changeAttributes){
        try {
            Product product = ProductManager.getInstance().findProductById(changeAttributes.getProductIdToRemove());
            off.getProducts().remove(product);
        } catch (NoSuchAProductException e) {

        }
    }


    private void acceptAssignComment(Request request) throws NoSuchAProductException {
        Comment comment = request.getComment();
        comment.setStatus(CommentStatus.VERIFIED);
        ProductManager.getInstance().assignAComment(comment.getId(),comment);
    }

    private void acceptSeller(Request request) {
        Seller seller = request.getSeller();
        seller.setVerified(true);
        DBManager.save(seller);
    }

    public void decline(int requestId) throws NoSuchARequestException {
        Request request = findRequestById(requestId);
        /* TODO : DELETE Extra Data from DB */
        request.setDone(true);
        request.setAccepted(false);
        DBManager.save(request);
    }

    public static RequestManager getInstance(){
        if(requestManager == null){
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    public ArrayList<Request> getRequestsForManager() {
        ArrayList<Request> toReturn = new ArrayList<>();
        DBManager.loadAllData(Request.class).forEach(request -> {
            if (!request.isDone()) toReturn.add(request);
        });
        return toReturn;
    }
}
