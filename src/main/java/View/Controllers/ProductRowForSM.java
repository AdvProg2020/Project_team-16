package View.Controllers;

import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.product.EditorIsNotSellerException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import com.jfoenix.controls.JFXButton;
import controler.ManagerController;
import controler.SellerController;
import javafx.event.ActionEvent;
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
        // TODO: 6/13/2020 Image
        canEditThis = CacheData.getInstance().getRole().equals("seller");
        name.setText(nameStr);
        id.setText("" + productId);
        editButt.setDisable(!canEditThis);
        idProduct = productId;
        buttonInitialize();
    }

    private void buttonInitialize() {
        showBut.setOnAction(e -> show());
        editButt.setOnAction(event -> editButtHandle());
        delete.setOnAction(event -> sendDeleteRequest());
    }

    private void editButtHandle() {
        // TODO: 6/20/2020 Completing
    }

    private void sendDeleteRequest() {
        if (canEditThis) {
            try {
                SellerController.getInstance().removeProduct(idProduct, CacheData.getInstance().getUsername());
            } catch (NoSuchACategoryException | NoSuchAProductInCategoryException | NoSuchAProductException | EditorIsNotSellerException e) {
                e.printStackTrace();
                new OopsAlert().show(e.getMessage());
            }
        } else {
            try {
                ManagerController.getInstance().removeProduct(idProduct);
            } catch (NoSuchACategoryException | NoSuchAProductInCategoryException | NoSuchAProductException e) {
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
