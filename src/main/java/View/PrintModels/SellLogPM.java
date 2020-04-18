package View.PrintModels;

import lombok.Data;

@Data
public class SellLogPM {
    private String product;
    private int moneyGotten;
    private int discount;
    private String buyer;
    private String deliveryStatus;
}
