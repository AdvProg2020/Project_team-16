package ModelPackage.Users;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private List<SubCart> subCarts;
    private long totalPrice;

    public Cart() {
        this.subCarts = new ArrayList<>();
    }
}
