package ModelPackage.Users;


import lombok.*;

import java.util.List;

@Data
public class Cart {
    private List<SubCart> subCarts;
    private long totalPrice;
}
