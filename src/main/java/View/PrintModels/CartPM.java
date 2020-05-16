package View.PrintModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CartPM {
    private long totalPrice;
    private ArrayList<InCartPM> purchases;

    public CartPM(long totalPrice, ArrayList<InCartPM> purchases) {
        this.totalPrice = totalPrice;
        this.purchases = purchases;
    }

    public void print(CartPM cartPM){
        if (purchases.isEmpty()){
            System.out.println("Your Cart is Currently Empty!");
        }
        System.out.println(String.format("There Are %d Good in Your Cart.\nTotal Price is %o", purchases.size(), totalPrice));
    }
}
