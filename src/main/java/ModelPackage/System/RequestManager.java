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
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import ModelPackage.Users.Advertise;
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
import java.util.Date;
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
                break;
            case CHANGE_PRODUCT:
                acceptEditProduct(request);
                break;
            case CREATE_OFF:
                acceptCreateOff(request);
                break;
            case EDIT_OFF:
                acceptEditOff(request);
                break;
            case ASSIGN_COMMENT:
                acceptAssignComment(request);
                break;
            case REGISTER_SELLER:
                acceptSeller(request);
                break;
            case ADVERTISE:
                acceptAdvertise(request);
        }
        request.setDone(true);
        request.setAccepted(true);
        DBManager.save(request);
    }

    private void acceptAdvertise(Request request) {
        String username = request.getUserHasRequested();
        Seller seller = DBManager.load(Seller.class, username);
        if (seller != null) {
            if (seller.getBalance() > 20) {
                seller.setBalance(seller.getBalance() - 20);
                Advertise ad = request.getAdvertise();
                //todo : Add to Timer
                ad.setActive(true);
                ad.setCreated(new Date());
                DBManager.save(ad);
                DBManager.save(seller);
            } else {
                MessageManager.getInstance().sendMessage(seller, "Ad Request Rejected",
                        "You Don't Have Enough Money to Create An Advertisement for your product.\n" +
                                "Refill Your Account Ant Try Late.\n" +
                                "\n" +
                                new Date() + "" +
                                "CFKala Manager");
            }
        }
    }

    private void acceptCreateProduct(Request request){
        Product product = request.getProduct();
        product.setProductStatus(ProductStatus.VERIFIED);
        DBManager.save(product);
        CategoryManager.getInstance().addProductToCategory(product,product.getCategory());
        Seller seller = DBManager.load(Seller.class, request.getUserHasRequested());
        // TODO: 6/20/2020 Filling request Field
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
        if (editAttribute.getPublicFeatures() != null) {
            editPublicFeatureProduct(product,editAttribute);
        }
        if (editAttribute.getSpecialFeatures() != null) {
            editSpecialFeatureProduct(product,editAttribute);
        }
        if (editAttribute.getNewCategoryId() != 0){
            try {
                CategoryManager.getInstance().editProductCategory(product.getId(),product.getCategory().getId(),editAttribute.getNewCategoryId());
            } catch (Exception ignore) {
            }
        }
        if (editAttribute.getNewPrice() != 0) {
            try {
                ProductManager.getInstance().changePrice(product, editAttribute.getNewPrice(), request.getUserHasRequested());
            } catch (NoSuchSellerException ignore) {
            }
        }
        if (editAttribute.getNewStock() != 0) {
            try {
                ProductManager.getInstance().changeStock(product, editAttribute.getNewStock(), request.getUserHasRequested());
            } catch (NoSuchSellerException ignore) {
            }
        }
    }

    private void editPublicFeatureProduct(Product product,ProductEditAttribute editAttribute){
        Map<String, String> publicFeatures = editAttribute.getPublicFeatures();
        Map<String, String> features = product.getPublicFeatures();
        publicFeatures.forEach((key, value) -> {
            if (features.containsKey(key)) {
                features.replace(key, value);
            }
        });
        DBManager.save(product);
    }

    private void editSpecialFeatureProduct(Product product,ProductEditAttribute editAttribute){
        Map<String, String> specialFeatures = editAttribute.getSpecialFeatures();
        Map<String, String> features = product.getPublicFeatures();
        specialFeatures.forEach((key, value) -> {
            if (features.containsKey(key)) {
                features.replace(key, value);
            }
        });
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
        // TODO: 6/22/2020 Alert Timer
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
        } catch (NoSuchAOffException ignore) {
        }
    }

    private void addProductToOff(Off off,OffChangeAttributes changeAttributes){
        try {
            // TODO: 6/20/2020 Diagnosis The Problem
            Product product = ProductManager.getInstance().findProductById(changeAttributes.getProductIdToAdd());
            off.getProducts().add(product);
            DBManager.save(product);
            Seller seller = off.getSeller();
            try {
                SellPackage sellPackage = seller.findPackageByProductId(product.getId());
                sellPackage.setOnOff(true);
                sellPackage.setOff(off);
                DBManager.save(sellPackage);
            } catch (NoSuchAPackageException e) {
                e.printStackTrace();
            }
            DBManager.save(seller);
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
        ProductManager.getInstance().assignAComment(comment.getProduct().getId(), comment);
    }

    private void acceptSeller(Request request) {
        Seller seller = request.getSeller();
        seller.setVerified(true);
        DBManager.save(seller);
    }

    public void decline(int requestId) throws NoSuchARequestException {
        Request request = findRequestById(requestId);
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
