package View.PrintModels;

import javafx.scene.image.Image;
import lombok.Data;

@Data
public class AdPM {
    int productId;
    String name;
    String seller;
    Image image;

    public AdPM(int productId, String name, String seller, Image image) {
        this.productId = productId;
        this.name = name;
        this.seller = seller;
        this.image = image;
    }
}