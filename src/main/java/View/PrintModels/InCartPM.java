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

    public void print(InCartPM inCartPM){
        System.out.println(
                String.format(
                                "-- %s     -%d     (%d) \n" +
                                "Brand : %s     Seller : %s\n" +
                                "About : %s"
                                , product.getName(), product.getId(), amount,
                                  product.getBrand(), sellerId,
                                  product.getDescription()
                )
        );
    }
}