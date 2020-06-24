package View.Controllers;

import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import com.jfoenix.controls.JFXButton;
import controler.ManagerController;
import controler.ProductController;
import controler.SellerController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ProductRowForSM {
    public ImageView image;
    public Label name;
    public Label id;
    public JFXButton showBut;
    public JFXButton editButt;
    public JFXButton delete;
    private int idProduct;
    private boolean canEditThis;

    private static String nameStr;
    private static int productId;

    public static Parent generate(String name, int id) throws IOException {
        nameStr = name;
        productId = id;
        return Main.loadFXML("ProductRow");
    }

    @FXML
    public void initialize(){
        canEditThis = CacheData.getInstance().getRole().equals("Seller") |
                CacheData.getInstance().getRole().equals("seller");
        name.setText(nameStr);
        id.setText("" + productId);
        editButt.setDisable(!canEditThis);
        idProduct = productId;
        image.setImage(ProductController.getInstance().loadMainImage(idProduct));
        buttonInitialize();
    }

    private void buttonInitialize() {
        showBut.setOnAction(e -> show());
        editButt.setOnAction(event -> editButtHandle());
        delete.setOnAction(event -> sendDeleteRequest());
    }

    private void editButtHandle() {
        try {
            CacheData.getInstance().setProductId(idProduct);
            Scene scene = new Scene(Main.loadFXML("EditProduct"));
            Main.setSceneToStage(new Stage(StageStyle.UNDECORATED), scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendDeleteRequest() {
        if (canEditThis) {
            try {
                SellerController.getInstance().removeProduct(idProduct, CacheData.getInstance().getUsername());
            } catch (NoSuchAPackageException e) {
                e.printStackTrace();
                new OopsAlert().show(e.getMessage());
            }
        } else {
            try {
                ManagerController.getInstance().removeProduct(idProduct);
            } catch (NoSuchAProductException e) {
                new OopsAlert().show(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void show() {
        CacheData.getInstance().setProductId(idProduct);
        Stage stage = new Stage();
        try {
            Scene scene = new Scene(Main.loadFXML("ProductDigest"));
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            Main.moveSceneOnMouse(scene, stage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
