package ModelPackage.Product;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Builder @Data @RequiredArgsConstructor
public class Category {
    private String name;
    private String id;
    private ArrayList<String> specialFeatures;
    private ArrayList<Category> subCategories;
    private String parentId;
    private ArrayList<String> allProductInThis;

    public Category(String name, String id, String parentId) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
        this.specialFeatures = new ArrayList<String>();
        this.subCategories  = new ArrayList<Category>();
        this.allProductInThis = new ArrayList<String>();
    }
}
