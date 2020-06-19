package View;

import View.PrintModels.CartPM;
import View.PrintModels.InCartPM;
import View.PrintModels.MiniProductPM;
import View.PrintModels.SellPackagePM;
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

    }
}

