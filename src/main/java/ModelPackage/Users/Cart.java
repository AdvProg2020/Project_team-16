package ModelPackage.Users;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private List<SubCart> subCarts;
    private long totalPrice;
    private String discountCode;

    public Cart() {
        subCarts = new ArrayList<>();
        discountCode = "";
    }
}
