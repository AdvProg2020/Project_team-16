package ModelPackage;

import ModelPackage.Product.Product;
import lombok.*;

@Data
@Builder
public class SubCart extends Cart {
    private Product product;
    private String productId;
    private String sellerId;
    private int amount;
}