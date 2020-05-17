package controler;

import ModelPackage.System.*;
import ModelPackage.Users.Manager;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Controller {
    protected AccountManager accountManager = AccountManager.getInstance();
    protected CartManager cartManager = CartManager.getInstance();
    protected CategoryManager categoryManager = CategoryManager.getInstance();
    protected CSCLManager csclManager = CSCLManager.getInstance();
    protected CustomerManager customerManager = CustomerManager.getInstance();
    protected DiscountManager discountManager = DiscountManager.getInstance();
    protected FilterManager filterManager = FilterManager.getInstance();
    protected ManagerManager managerManager = ManagerManager.getInstance();
    protected OffManager offManager = OffManager.getInstance();
    protected ProductManager productManager = ProductManager.getInstance();
    protected RequestManager requestManager = RequestManager.getInstance();
    protected SellerManager sellerManager = SellerManager.getInstance();
    protected SortManager sortManager = SortManager.getInstance();
}
