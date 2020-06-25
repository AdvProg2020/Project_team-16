package View.PrintModels;

import javafx.scene.image.Image;
import lombok.Data;

import java.util.Date;

@Data
public class OffProductPM {
    private String name;
    private int id;
    private int offPrice;
    private int percent;
    private Image image;
    private Date end;

    public OffProductPM(String name, int id, int offPrice, int percent, Image image, Date end) {
        this.name = name;
        this.id = id;
        this.offPrice = offPrice;
        this.percent = percent;
        this.image = image;
        this.end = end;
    }
}
