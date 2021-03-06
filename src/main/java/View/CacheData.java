package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

@Data
public class CacheData {
    private static CacheData cacheData;
    public static CacheData getInstance() { return cacheData; }
    static { cacheData = new CacheData(); }

    public CacheData() {
        filters = new FilterPackage();
        sorts = new SortPackage();
        cart = new NotSignedInCart();
        roleProperty = new SimpleStringProperty("");
        companyID = 0;
        username = "";
        role = "";
        productId = 2;
    }

    public StringProperty roleProperty;
    private String username;
    private String role;
    private int companyID;
    private int productId;
    private String[] signUpData;
    private NotSignedInCart cart;
    private FilterPackage filters;
    private SortPackage sorts;

    public void logout(){
        roleProperty.set("");
        role = "";
        username = "";
        reset();
        cart = new NotSignedInCart();
    }

    public void reset(){
        sorts.reset();
        filters.reset();
    }

    public void setRole(String role) {
        this.role = role;
        roleProperty.setValue(role);
    }
}
