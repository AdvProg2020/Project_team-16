package View.PrintModels;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderProductPM {
    private int id;
    private String name;
    private String seller;
    private long price;

    public OrderProductPM(int id, String name, String seller, long price) {
        this.id = id;
        this.name = name;
        this.seller = seller;
        this.price = price;
    }
}
