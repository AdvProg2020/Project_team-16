package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Product;
import ModelPackage.Product.ProductStatus;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;
import ModelPackage.Users.Seller;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {
    private List<Request> requests;
    private static RequestManager requestManager = null;

    private RequestManager(){
        requests =  new ArrayList<>();
    }

    public void addRequest(Request request){
        requests.add(request);
        /* TODO : Add in database */
    }

    public Request findRequestById(String id){
        for (Request request : requests) {
            if(request.getRequestId().equals(id))return request;
        }
        return null;
    }

    public void accept(String requestId){
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
            case REGIDTER_SELLER:
                acceptSeller(request);
                break;
        }
    }

    private void acceptCreateProduct(Request request){
        Product product = request.getProduct();
        product.setProductStatus(ProductStatus.VERIFIED);
        ProductManager.getInstance().addProductToList(product);
    }

    private void acceptEditProduct(Request request){
        Product product = request.getProduct();
        product.setProductStatus(ProductStatus.VERIFIED);
        ProductManager productManager = ProductManager.getInstance();
        Product toChange = productManager.findProductById(product.getProductId());
        ArrayList<Product> products = productManager.getAllProducts();
        products.set(products.indexOf(toChange),product);
        productManager.setAllProducts(products);
    }

    private void acceptCreateOff(Request request){
        Off off = request.getOff();
        Seller seller = off.getSeller();
        List<Off> offs = seller.getOffs();
        offs.add(off);
        seller.setOffs(offs);
    }

    private void acceptEditOff(Request request){
        Off off = request.getOff();
        Seller seller = off.getSeller();
        List<Off> offs = seller.getOffs();
        Off offf = off;/* TODO : = CLCSManager.getInstance().findOffById(off.getId());*/
        offs.set(offs.indexOf(offf),off);
    }

    private void acceptAssignComment(Request request){
        Comment comment = request.getComment();
        comment.setStatus(CommentStatus.VERIFIED);
        ProductManager.getInstance().assignAComment(comment.getProductId(),comment);
    }

    private void acceptSeller(Request request) {
        Seller seller = request.getSeller();
        /*TODO :
         *  1. save in database
         *  2. load it into AccountManager */
    }

    public void decline(String requestId){
        Request request = findRequestById(requestId);
        /* TODO : remove from database */
        requests.remove(request);
    }

    public static RequestManager getInstance(){
        if(requestManager == null){
            return requestManager = new RequestManager();
        }
        else {
            return requestManager;
        }
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void clear(){
        requests.clear();
    }
}
