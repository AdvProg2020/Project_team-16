package View.Controllers;

import View.CacheData;
import View.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;

import static View.Controllers.Notification.show;

public class MainPage {
    public JFXButton account;
    public JFXButton cart;
    public JFXButton close;
    public JFXButton minimize;
    public JFXButton Products;
    public JFXButton offs;
    public VBox mainBox;

    private PopOver accountPopOver;
    private static CacheData cacheData = CacheData.getInstance();

    @FXML
    public void initialize(){
        buttons();
        popOver();
        binds();
        listeners();
        /*
        slider();
        advertising();
        menus();
        */
    }

    private void slider() {

    }

    private void listeners() {
        cacheData.roleProperty.addListener((v, oldValue, newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    accountPopOver.setContentNode(Main.loadFXML("accountManagerPopUp"));
                } else {
                    accountPopOver.setContentNode(Main.loadFXML("accountPopUp"));
                }
            } catch (IOException e) {
                show("Error", e.getMessage(), close.getScene().getWindow(), true);
                e.printStackTrace();
            }
        });
    }

    private void popOver() {
        accountPop();
    }

    private void accountPop() {
        String fxml;
        if (CacheData.getInstance().getRole().isEmpty()) {
            fxml = "accountPopUp";
        } else {
            fxml = "accountManagerPopUp";
        }
        try {
            accountPopOver = new PopOver(Main.loadFXML(fxml));
            accountPopOver.setTitle("Account");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void binds() {
        cart.disableProperty().bind(cacheData.roleProperty.isEqualTo("Customer").not());
        cart.disableProperty().bind(Bindings.isEmpty(cacheData.roleProperty).not());
    }

    private void buttons() {
        close.setOnAction(event -> ((Stage) close.getScene().getWindow()).close());
        minimize.setOnAction(event -> ((Stage) close.getScene().getWindow()).setIconified(true));
        cart.setOnAction(event -> gotoCart());
        account.setOnAction(event -> accountManager());
    }

    private void accountManager() {
        if (accountPopOver.isShowing()) {
            accountPopOver.hide();
        } else {
            accountPopOver.show(account);
        }
    }

    private void gotoCart() {
        try {
            Scene scene = new Scene(Main.loadFXML("Cart"));
            Stage stage = (Stage) cart.getScene().getWindow();
            stage.setScene(scene);
            Main.moveSceneOnMouse(scene, stage);
        } catch (IOException e) {
            show("Error", e.getMessage(), cart.getScene().getWindow(), true);
            e.printStackTrace();
        }
    }
}