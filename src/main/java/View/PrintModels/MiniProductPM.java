package View.PrintModels;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MiniProductPM {
    private String name;
    private int id;
    private String categoryName;
    private List<SellPackagePM> sellPackagePMs;
    private String brand;
    private double score;
    private String description;

    public MiniProductPM(String name, int id, String categoryName, String brand, double score, String description, List<SellPackagePM> sellPackages) {
        this.name = name;
        this.id = id;
        this.categoryName = categoryName;
        this.brand = brand;
        this.score = score;
        this.description = description;
        sellPackagePMs = sellPackages;
    }
}
