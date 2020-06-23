package View.Controllers;

import View.CacheData;
import View.Main;
import View.PrintModels.AdPM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Random;

import static View.Controllers.Content.COLORS;

public class Advertise {
    @FXML
    private Circle image;
    @FXML
    private Label name;
    @FXML
    private Label seller;
    @FXML
    private AnchorPane root;

    public static Parent createAdvertise(AdPM pm) {
        FXMLLoader loader = Main.getFXMLLoader("Advertise");
        try {
            Parent parent = loader.load();
            Advertise controller = loader.getController();
            controller.initData(pm);
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void initialize() {
        root.setStyle("-fx-background-color: " + getRandomColor() + ";");
    }

    private void initData(AdPM pm) {
        name.setText(pm.getName());
        seller.setText(pm.getSeller());
        root.setOnMouseClicked(event -> gotoProduct(pm.getProductId()));
        // TODO: 6/23/2020 Pic Not Showing
        image.setFill(new ImagePattern(pm.getImage()));
    }

    private void gotoProduct(int productId) {
        CacheData.getInstance().setProductId(productId);
        try {
            Scene scene = new Scene(Main.loadFXML("ProductDigest", "MainPage"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            Main.moveSceneOnMouse(scene, stage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRandomColor() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }
}
