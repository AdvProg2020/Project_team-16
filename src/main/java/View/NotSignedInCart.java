package View;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotSignedInCart {
    CartPM cart;

    public NotSignedInCart(){
        cart = new CartPM();
    }

    public void addToCart(MiniProductPM miniProductPM, SellPackagePM pm) {
        // TODO: 6/19/2020
    }
}

