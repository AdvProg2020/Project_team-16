package View.PrintModels;

import ModelPackage.Maps.SellerIntegerMap;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data @Builder
public class MiniProductPM {
    private String name;
    private int id;
    private String categoryName;
    private List<SellerIntegerMap> sellers;
    private String brand;
    private double score;
    private String description;

    public MiniProductPM(String name, int id, String categoryName, List<SellerIntegerMap> sellers, String brand, double score, String description) {
        this.name = name;
        this.id = id;
        this.categoryName = categoryName;
        this.sellers = sellers;
        this.brand = brand;
        this.score = score;
        this.description = description;
    }

    public void print(MiniProductPM miniProductPM){
        System.out.println(String.format(
                "%s     -%d-    '%s'\n" +
                "Sellers : " ,
                miniProductPM.name, miniProductPM.id,
                miniProductPM.categoryName
        ));
        for (SellerIntegerMap seller : sellers) {
            System.out.println(String.format("%s - "));
        }
        System.out.println("\b\n");
        System.out.println(String.format("Brand : %s    Average Score : %s\nAbout : %s\n", brand, score, description));
    }
}
