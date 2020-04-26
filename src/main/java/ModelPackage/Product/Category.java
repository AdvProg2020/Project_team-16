package ModelPackage.Product;


import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class Category {
    private String name;
    private String id;
    private ArrayList<String> specialFeatures;
    private ArrayList<Category> subCategories;
    private String parentId;
    private ArrayList<String> allProductInThis;

    public Category(String name, String parentId) {
        this.name = name;
        this.id = idGenerator();
        this.parentId = parentId;
        this.specialFeatures = new ArrayList<String>();
        this.subCategories  = new ArrayList<Category>();
        this.allProductInThis = new ArrayList<String>();

    }

    private String idGenerator(){
        Date date = new Date();
        return String.format("CT%s%04d",date.toString().replaceAll("\\s","").replaceAll(":",""),(int)(Math.random()*9999+1));
    }
}
