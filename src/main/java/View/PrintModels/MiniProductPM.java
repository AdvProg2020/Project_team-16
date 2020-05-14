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
    private List<SellerIntegerMap> sellers;
    private String brand;
    private double score;
    private String description;

    public MiniProductPM(String name, int id, List<SellerIntegerMap> sellers, String brand, double score, String description) {
        this.name = name;
        this.id = id;
        this.sellers = sellers;
        this.brand = brand;
        this.score = score;
        this.description = description;
    }
}
