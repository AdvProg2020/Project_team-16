package View.PrintModels;

import lombok.Data;

@Data
public class ProductsOnOffPM {
    private int id;
    private String name;
    private int realPrice;
    private int afterOffPrice;

    public ProductsOnOffPM(int id, String name, int realPrice, int afterOffPrice) {
        this.id = id;
        this.name = name;
        this.realPrice = realPrice;
        this.afterOffPrice = afterOffPrice;
    }
}
