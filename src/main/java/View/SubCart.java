package View;

import lombok.Data;

@Data
public class SubCart{
    private int productId;
    private int amount;

    SubCart(int productId,int amount){
        this.productId = productId;
        this.amount = amount;
    }
}
