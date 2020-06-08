package ModelPackage.Product;
import ModelPackage.Off.Off;
import ModelPackage.Users.Seller;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: 6/8/2020 DB
@Data @NoArgsConstructor
public class SellPackage {
    private Product product;
    private Seller seller;
    private int price;
    private int stock;
    private Off off;
    private boolean isOnOff;
    private boolean isAvailable;

    public SellPackage(Product product, Seller seller, int price, int stock, Off off, boolean isOnOff, boolean isAvailable) {
        this.product = product;
        this.seller = seller;
        this.price = price;
        this.stock = stock;
        this.off = off;
        this.isOnOff = isOnOff;
        this.isAvailable = isAvailable;
    }
}
