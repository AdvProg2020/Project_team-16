package View.Controllers;

import View.CacheData;
import View.Main;
import View.PrintModels.MiniProductPM;
import controler.ProductController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    public ImageView soldOut;

    public static Parent generate(MiniProductPM pm) throws IOException {
        FXMLLoader loader = Main.getFXMLLoader("SingleMiniProduct");
        Parent parent = loader.load();
        SingleMiniProduct controller = loader.getController();
        controller.initData(pm);
        return parent;
    }

    @FXML
    private void initialize() {

    }

    private void initData(MiniProductPM pm) {
        int offPrc = pm.getOffPrice();
        int prc = pm.getPrice();
        String name = pm.getName();
        int id = pm.getId();
        boolean onOff = (offPrc != 0);
        specialOffer.setVisible(onOff);
        offPrice.setText("Offer : " + offPrc + "$");
        price.setText("" + prc + "$");
        productName.setText(name);
        offPrice.setVisible(onOff);
        pane.setOnMouseClicked(e -> {
            CacheData.getInstance().setProductId(id);
            try {
                Scene scene = new Scene(Main.loadFXML("ProductDigest", "MainPage", "ProductsPage"));
                Main.setSceneToStage(pane, scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        productPreViewImage.setImage(ProductController.getInstance().loadMainImage(id));
        soldOut.setVisible(!pm.isAvailable());
    }
}
