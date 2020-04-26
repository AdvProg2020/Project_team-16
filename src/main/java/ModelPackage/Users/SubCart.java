package ModelPackage.Users;

import ModelPackage.Product.Product;
import lombok.*;

@Data @NoArgsConstructor
public class SubCart{
    private Product product;
    private String productId;
    private String sellerId;
    private int amount;
}
