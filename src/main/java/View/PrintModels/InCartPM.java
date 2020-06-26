package View.PrintModels;

import lombok.Data;

@Data
public class InCartPM{
    private MiniProductPM product;
    private String productName;
    private String sellerId;
    private int price;
    private int offPrice;
    private int amount;
    private int total;

    public InCartPM(MiniProductPM product, String sellerId, int price, int offPrice, int amount) {
        this.product = product;
        productName = product.getName();
        this.sellerId = sellerId;
        this.price = price;
        this.offPrice = offPrice;
        this.amount = amount;
        total = amount * (offPrice == 0 ? price : offPrice);
    }

    public void setAmount(int amount) {
        this.amount = amount;
        total = amount * (offPrice == 0 ? price : offPrice);
    }
}