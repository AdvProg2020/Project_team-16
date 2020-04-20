package ModelPackage.Users;

import ModelPackage.Off.Off;
import ModelPackage.Product.Comment;
import ModelPackage.Product.Product;
import lombok.Data;

import java.util.Date;

@Data
public class Request {
    String usernameHasRequested;
    String requestId;
    RequestType requestType;
    String request;
    Off off;
    Product product;
    Comment comment;

    public Request(String usernameHasRequested, RequestType requestType, String request,Object toChange) {
        this.usernameHasRequested = usernameHasRequested;
        this.requestType = requestType;
        this.request = request;

        String className = toChange.getClass().getName();
        if(className.equals("ModelPackage.Off.Off")){
            off = (Off)toChange;
        }else if(className.equals("ModelPackage.Product.Comment")){
            comment = (Comment)toChange;
        }else if((className.equals("ModelPackage.Product.Product"))){
            product = (Product) toChange;
        }

        this.requestId = generateId();
    }

    private String generateId(){
        Date date = new Date();
        return String.format("REQ%s%04d",date.toString().replaceAll("\\s","".replaceAll(":","")),(int)(Math.random()*9999+1));
    }
}
