package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data
public class OrderLogPM {
    private Date date;
    private MiniProductPM[] productPMs;
    private String deliveryStatus;
    private int price;
    private int paid;
    private int discount;
}
