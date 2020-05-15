package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data
public class InCartPM{
    private MiniProductPM product;
    private String sellerId;
    private int amount;

    public InCartPM(MiniProductPM product, String sellerId, int amount) {
        this.product = product;
        this.sellerId = sellerId;
        this.amount = amount;
    }
}