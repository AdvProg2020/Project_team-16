package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class InCartPM extends CartPM {
    private MiniProductPM product;
    private String sellerId;
    private int amount;
}
