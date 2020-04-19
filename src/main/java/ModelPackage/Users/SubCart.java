package ModelPackage.Users;

import ModelPackage.Product.Product;
import lombok.*;

@Data
@Builder
public class SubCart{
    private Product product;
    private String productId;
    private String sellerId;
    private int amount;
}
