package controler;

import ModelPackage.Log.SellLog;
import ModelPackage.Product.Company;
import View.PrintModels.CompanyPM;
import View.PrintModels.SellLogPM;

import java.util.*;

public class SellerContoller extends Controller{

     public CompanyPM viewCompanyInfo(String sellerUserName) {
         Company company = sellerManager.viewCompanyInformation(sellerUserName);
         return new CompanyPM(company.getName(), company.getGroup(), company.getPhone());
     }

    public List<SellLogPM> viewSalesHistory(String sellerUserName) {
        List<SellLog> sellLogs = sellerManager.viewSalesHistory(sellerUserName);
        ArrayList<SellLogPM> sellLogPMs = new ArrayList<>();
        for (SellLog sellLog : sellLogs) {
            sellLogPMs.add(new SellLogPM(sellLog.getProduct().getId(), sellLog.getMoneyGotten(),
                    sellLog.getDiscount(), sellLog.getBuyer().getUsername(), sellLog.getDeliveryStatus()))
        }
        return sellLogPMs;
    }

}
