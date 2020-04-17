package View.PrintModels;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data @Builder

public class CartPM {
    private int totalPrice;
    private ArrayList<InCartPM> purchases;
}
