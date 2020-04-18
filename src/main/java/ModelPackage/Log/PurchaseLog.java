package ModelPackage.Log;

import ModelPackage.Product.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Setter @Getter
public class PurchaseLog extends Log {
    private HashMap<Product,Integer> productsAndItsPrices;
    private int pricePaid;
    private int discount;
    private HashMap<Seller,Integer> sellers;
}
