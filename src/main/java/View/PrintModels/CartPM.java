package View.PrintModels;

import ModelPackage.Users.Cart;
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

    public CartPM() {
        purchases = new ArrayList<>();
    }

    public void addInCart(MiniProductPM miniProductPM, String sellerId, int price, int offPrice, int amount) {

    }

    public void increase() {

    }

    public void decrease() {

    }
}
