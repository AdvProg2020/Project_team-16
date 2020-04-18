package ModelPackage.System;

import ModelPackage.Product.Product;
import lombok.Data;

import java.util.ArrayList;

@Data
public class ProductManager {
    private static ArrayList<Product> allProducts;
    private static ProductManager productManager = null;

    public ProductManager(){
        allProducts = new ArrayList<Product>();
    }

    public static ProductManager getInstance(){
        if(productManager == null){
            return productManager = new ProductManager();
        }
        else{
            return productManager;
        }
    }
}
