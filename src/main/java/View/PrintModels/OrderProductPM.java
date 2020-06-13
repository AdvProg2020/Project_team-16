package View.PrintModels;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderProductPM {
    private String name;
    private String seller;
    private long price;

    public OrderProductPM(String name, String seller, long price) {
        this.name = name;
        this.seller = seller;
        this.price = price;
    }
}
