package View.Controllers;

import View.CacheData;
import View.Main;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SingleMiniProduct {
    public ImageView productPreViewImage;
    public ImageView specialOffer;
    public Label productName;
    public Label price;
    public Label offPrice;
    public Pane pane;
    private int id;

    private static int prc;
    private static int offPrc;
    private static String name;
    private static int ID;

    public static Parent generate(int id, String name, int price) throws IOException {
        ID = id;
        prc = price;
        offPrc = 0;
        SingleMiniProduct.name = name;
        return Main.loadFXML("SingleMiniProduct");
    }

    public static Parent generate(int id, String name, int price, int offPrice) throws IOException {
        ID = id;
        SingleMiniProduct.name = name;
        prc = price;
        offPrc = offPrice;
        return Main.loadFXML("SingleMiniProduct");
    }

    @FXML
    public void initialize() {
        id = ID;
        boolean onOff = (offPrc != 0);
        specialOffer.setVisible(onOff);
        offPrice.setText("" + offPrc);
        price.setText("" + prc);
        productName.setText(name);
        price.setVisible(onOff);
        pane.setOnMouseClicked(e -> {
            CacheData.getInstance().setProductId(id);
            Stage stage = (Stage) pane.getScene().getWindow();
            try {
                stage.setScene(new Scene(Main.loadFXML("ProductDigest")));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
