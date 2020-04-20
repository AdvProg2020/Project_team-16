package ModelPackage.System;

import ModelPackage.Product.Product;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;
import org.junit.Assert;
import org.junit.Test;

public class RequestManagerTest {
    private RequestManager requestManager;
    private Request request;

    {
        requestManager = RequestManager.getInstance();
        request = new Request("asghar",RequestType.CREATE_PRODUCT,
                "adsfasdf asdf",new Product());

        requestManager.clear();
        requestManager.addRequest(request);
    }

    @Test
    public void getInstanceTest(){
        RequestManager test = RequestManager.getInstance();

        Assert.assertEquals(requestManager,test);
    }

    @Test
    public void addRequestTest(){
        requestManager.clear();
        requestManager.addRequest(request);
        int actual = requestManager.getRequests().size();
        Assert.assertEquals(1,actual);
    }

    @Test
    public void findRequestById(){
        Request request = requestManager.findRequestById(this.request.getRequestId());
        Assert.assertEquals(this.request,request);
    }

    @Test
    public void findRequestByIdNull(){
        Request request = requestManager.findRequestById("RQ;adkfljasdjflsdjkfj;askldjf");
        Assert.assertNull(request);
    }
}
