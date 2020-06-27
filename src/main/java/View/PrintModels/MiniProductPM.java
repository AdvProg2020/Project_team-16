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
    private boolean available;

    public MiniProductPM(String name, int id, String categoryName, String brand, double score, String description, List<SellPackagePM> sellPackages, boolean available) {
        this.name = name;
        this.id = id;
        this.categoryName = categoryName;
        this.brand = brand;
        this.score = score;
        this.description = description;
        sellPackagePMs = sellPackages;
        this.available = available;
    }

    @Override
    public String toString() {
        return name;
    }

    public SellPackagePM findPackageForSeller(String username) {
        for (SellPackagePM sellPackagePM : sellPackagePMs) {
            if (sellPackagePM.getSellerUsername().equals(username)) return sellPackagePM;
        }
        return null;
    }

    public int getPrice() {
        int price = 20000000;
        for (SellPackagePM pm : sellPackagePMs) {
            if (pm.getPrice() < price) price = pm.getPrice();
        }
        return price == 20000000 ? -1 : price;
    }

    public int getOffPrice() {
        int ofPrc = 20000000;
        for (SellPackagePM pm : sellPackagePMs) {
            if (pm.getOffPercent() != 0) {
                int price = pm.getPrice() * (100 - pm.getOffPercent()) / 100;
                if (ofPrc > price) {
                    ofPrc = price;
                }
            }
        }
        return ofPrc == 20000000 ? 0 : ofPrc;
    }
}
