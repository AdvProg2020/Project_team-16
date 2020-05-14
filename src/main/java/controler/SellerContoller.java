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

}
