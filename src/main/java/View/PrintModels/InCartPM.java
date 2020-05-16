package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data
public class InCartPM{
    private MiniProductPM product;
    private String sellerId;
    private int price;
    private int offPrice;
    private int amount;

    public InCartPM(MiniProductPM product, String sellerId, int price, int offPrice, int amount) {
        this.product = product;
        this.sellerId = sellerId;
        this.price = price;
        this.offPrice = offPrice;
        this.amount = amount;
    }
}