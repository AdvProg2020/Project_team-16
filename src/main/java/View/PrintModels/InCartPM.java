package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data
public class InCartPM{
    private MiniProductPM product;
    private String sellerId;
    private int amount;
}