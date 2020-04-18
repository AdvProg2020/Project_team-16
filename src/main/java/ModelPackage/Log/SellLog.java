package ModelPackage.Log;

import ModelPackage.Product.Product;
import lombok.*;

@Setter @Getter @Builder @RequiredArgsConstructor
public class SellLog extends Log {
    @NonNull private Product product;
    @NonNull private int moneyGotten;
    @NonNull private int discount;
    @NonNull private User buyer;
}
