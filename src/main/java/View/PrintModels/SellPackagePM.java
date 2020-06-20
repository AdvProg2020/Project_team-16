package View.PrintModels;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SellPackagePM {
    private int offPercent;
    private int price;
    private int stock;
    private String sellerUsername;
    private boolean isAvailable;

    public SellPackagePM(int offPercent, int price, int stock, String sellerUsername, boolean isAvailable) {
        this.offPercent = offPercent;
        this.price = price;
        this.stock = stock;
        this.sellerUsername = sellerUsername;
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return sellerUsername;
    }
}
