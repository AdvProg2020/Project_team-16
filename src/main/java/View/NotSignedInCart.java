package View;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotSignedInCart {
    List<SubCart> subCarts;

    public NotSignedInCart(){
        subCarts = new ArrayList<>();
    }

    public void addToCart(){
        Printer.printMessage("Enter Amount : ");
        String amount = Scan.getInstance().getInteger();
        subCarts.add(new SubCart(View.Data.getInstance().getProductSeeingInId(),Integer.parseInt(amount)));
    }

    public boolean isEmpty(){
        return subCarts.isEmpty();
    }

    void clear(){
        subCarts.clear();
    }
}

