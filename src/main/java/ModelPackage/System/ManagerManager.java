package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Users.Manager;
import ModelPackage.Users.Request;
import ModelPackage.Users.User;

import java.util.List;

public class ManagerManager {
    private static ManagerManager managerManager = null;
    private ManagerManager(){ }
    public static ManagerManager getInstance(){
        if (managerManager == null)
            managerManager = new ManagerManager();
        return managerManager;
    }

    private List<Request> allRequests;
    private Manager manager;

    public void createManagerProfile(){}

    public void deleteUser(User user){}

    public void viewRequestDetails(String requestId){}

    public void acceptRequest(String requestId){}

    public void declineRequest(String requestId){}

    public List<DiscountCode> viewDiscountCodes(){}

    public DiscountCode getDiscountByCode(String code){}

    public void editDiscountCode(DiscountCode discountCode){}

    public void removeDiscountCode(DiscountCode discountCode){}


}
