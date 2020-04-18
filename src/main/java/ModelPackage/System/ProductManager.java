package ModelPackage.System;

import ModelPackage.Product.Product;
import ModelPackage.Users.Seller;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void addAmountOfStock(Product product, String sellerId,int amount){
        /* TODO : Add Exception of Negative Stock */
        HashMap<String, Integer> stock = product.getStock();
        stock.replace(sellerId, stock.get(sellerId) + amount);
        product.setStock(stock);
    }

    public Product findProductById(String id){
        /* TODO : NULL POINTER EXCEPTION */
        for (Product product : allProducts) {
            if(product.getProductId().equals(id)) return product;
        }
        return null;
    }

    public Product[] findProductByName(String name){
        List<Product> list = new ArrayList<>();
        for (Product product : allProducts) {
            if(product.getName().toLowerCase().contains(name.toLowerCase()))list.add(product);
        }
        Product[] toReturn = new Product[list.size()];
        list.toArray(toReturn);
        return toReturn;
    }

    public void addProductToList(Product product){
        allProducts.add(product);
    }
}
