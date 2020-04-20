package ModelPackage.System;

import ModelPackage.Users.Request;

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
