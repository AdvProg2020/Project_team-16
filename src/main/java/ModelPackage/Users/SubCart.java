package ModelPackage.Users;

import ModelPackage.Product.Product;
import lombok.*;

@Data @NoArgsConstructor
public class SubCart{
    private Product product;
    private String productId;
    private String sellerId;
    private int amount;

    public SubCart(Product product, String productId, String sellerId, int amount) {
        this.product = product;
        this.productId = productId;
        this.sellerId = sellerId;
        this.amount = amount;
    }
}
