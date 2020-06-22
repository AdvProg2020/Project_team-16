package View.Controllers;

import View.CacheData;
import View.Main;
import controler.AccountController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class AccountManagerPopUp {
    public Circle image;
    public Label username;
    public Label role;
    public Button gotoAccount;

    private static final String userPhoto = "/Images/user-png-icon-male-user-icon-512.png";

    @FXML
    public void initialize() {
        username.setText(CacheData.getInstance().getUsername());
        role.setText(CacheData.getInstance().getRole());
        loadImage();
        gotoAccount.setOnAction(event -> gotoManager());
    }

    private void gotoManager() {
        Stage stage = ((Stage) Stage.getWindows().filtered(Window::isFocused).get(0));
        Scene scene = null;
        try {
            switch (CacheData.getInstance().getRole()) {
                case "Manager":
                    scene = new Scene(Main.loadFXML("ManagerAccount"));
                    break;
                case "Customer":
                    scene = new Scene(Main.loadFXML("CustomerAccount"));
                    break;
                case "seller":
                    scene = new Scene(Main.loadFXML("SellerAccount"));
                    break;
            }
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImage() {
        Image image = AccountController.getInstance().userImage(username.getText());
        if (image == null) {
            image = new Image(userPhoto);
        }
        this.image.setFill(new ImagePattern(image));
    }
}
