package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.Product.Comment;
import ModelPackage.Product.CommentStatus;
import ModelPackage.Product.Product;
import ModelPackage.Product.ProductStatus;
import ModelPackage.System.database.DBManager;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;
import ModelPackage.Users.Seller;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
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
        DBManager.save(request);
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
            case REGISTER_SELLER:
                acceptSeller(request);
                break;
        }
    }

    private void acceptCreateProduct(Request request){
        Product product = request.getProduct();
        product.setProductStatus(ProductStatus.VERIFIED);
        ProductManager.getInstance().addProductToList(product);

        Seller seller = request.getSeller();
        seller.getProductIds().add(product.getProductId());
    }

    private void acceptEditProduct(Request request){
        Product product = request.getProduct();
        product.setProductStatus(ProductStatus.VERIFIED);
        ProductManager productManager = ProductManager.getInstance();
        Product toChange = productManager.findProductById(product.getId());
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
        ProductManager.getInstance().assignAComment(comment.getId(),comment);
    }

    private void acceptSeller(Request request) {
        Seller seller = request.getSeller();
        AccountManager.getInstance().getUsers().add(seller);

        String sellerJson = new Gson().toJson(seller);
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/users.user",true);
            fileWriter.write(sellerJson);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decline(String requestId){
        Request request = findRequestById(requestId);
        /* TODO : remove from database */
        requests.remove(request);
    }

    public static RequestManager getInstance(){
        if(requestManager == null){
            requestManager = new RequestManager();
        }
        return requestManager;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void clear(){
        requests.clear();
    }
}
