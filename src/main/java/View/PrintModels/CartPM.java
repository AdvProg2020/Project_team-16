package View.PrintModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CartPM {
    private int totalPrice;
    private ArrayList<InCartPM> purchases;
}
