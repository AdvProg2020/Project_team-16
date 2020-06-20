package View;

import lombok.Data;

@Data
public class CacheData {
    private static CacheData cacheData;
    public static CacheData getInstance() { return cacheData; }
    static { cacheData = new CacheData(); }

    public CacheData() {
        filters = new FilterPackage();
        sorts = new SortPackage();
        companyID = 0;
        username = "seller1";
        role = "seller";
        productId = 1;
    }

    private String username;
    private String role;
    private int companyID;
    private int productId;
    private String[] signUpData;
    private NotSignedInCart cart;
    private FilterPackage filters;
    private SortPackage sorts;

    public void logout(){

    }

    public void reset(){
        sorts.reset();
        filters.reset();
    }
}
