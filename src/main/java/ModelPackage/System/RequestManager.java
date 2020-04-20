package ModelPackage.System;

import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Product;
import ModelPackage.Product.ProductStatus;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;

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
            case EDIT_OFF:
                acceptCreateOrEditOff(request);
                break;
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

    private void acceptCreateOrEditOff(Request request){

    }

    private void acceptAssignComment(Request request){
        Comment comment = request.getComment();
        comment.setStatus(CommentStatus.VERIFIED);
        ProductManager.getInstance().assignAComment(comment.getProductId(),comment);
    }

    private void acceptSeller(Request request){

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
