package controler;

import ModelPackage.Log.SellLog;
import ModelPackage.Product.Company;
import View.PrintModels.CompanyPM;
import View.PrintModels.SellLogPM;

import java.util.List;

public class SellerContoller extends Controller{

     public CompanyPM viewCompanyInfo(String sellerUserName) {
         Company company = sellerManager.viewCompanyInformation(sellerUserName);
         return new CompanyPM(company.getName(), company.getGroup(), company.getPhone());
     }

    public SellLogPM[] viewSalesHistory(String sellerUserName) {
        List<SellLog> sellLogs = sellerManager.viewSalesHistory(sellerUserName);
        SellLogPM[] sellLogPMs = new SellLogPM[sellLogs.size()];
        for (int i = 0; i < sellLogPMs.length; i++) {
            sellLogPMs[i] = new SellLogPM(sellLogs.get(i).getProduct().getId(),
                    sellLogs.get(i).getMoneyGotten(), sellLogs.get(i).getDiscount(),
                    sellLogs.get(i).getBuyer().getUsername(), sellLogs.get(i).getDeliveryStatus());
        }
        return sellLogPMs;
    }

}
