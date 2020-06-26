package View.Controllers;

import ModelPackage.Users.MainContent;
import View.CacheData;
import View.Main;
import View.PrintModels.AdPM;
import View.Sound;
import View.SoundCenter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controler.ContentController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;
import org.jgroups.protocols.MAKE_BATCH;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static View.Controllers.Notification.show;
import static View.Sound.*;

public class MainPage extends BackAbleController {
    public JFXButton account;
    public JFXButton cart;
    public JFXButton close;
    public JFXButton minimize;
    public JFXButton products;
    public VBox mainBox;

    private PopOver accountPopOver;
    private static CacheData cacheData = CacheData.getInstance();

    @FXML
    public void initialize(){
        buttons();
        popOver();
        binds();
        listeners();
        slider();
        advertising();
        menus();
    }

    private void menus() {
        products.setOnAction(event -> gotoProducts());
    }

    private void gotoProducts() {
        try {
            Scene scene = new Scene(Main.loadFXML("ProductsPage", "MainPage"));
            Main.setSceneToStage(products, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void advertising() {
        List<Node> nodes = new ArrayList<>();
        List<AdPM> pms = ContentController.getInstance().getAds();
        pms.forEach(pm -> {
            Parent parent = Advertise.createAdvertise(pm);
            if (parent != null) {
                nodes.add(parent);
            }
        });
        Parent slideshow = SlideShow.makeSlideShow(nodes);
        mainBox.getChildren().add(slideshow);
    }

    private void slider() {
        List<Node> nodes = new ArrayList<>();
        List<MainContent> contents = ContentController.getInstance().getMainContents();
        contents.forEach(content -> {
            Parent parent = Content.createContent(content);
            if (parent != null) {
                nodes.add(parent);
            }
        });
        Parent slideshow = SlideShow.makeSlideShow(nodes);
        mainBox.getChildren().add(slideshow);
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
            accountPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void binds() {
        cart.disableProperty().bind(cacheData.roleProperty.isEqualTo("Customer").not());
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
            SoundCenter.play(POP_UP);
            accountPopOver.show(account);
        }
    }

    private void gotoCart() {
        try {
            Scene scene = new Scene(Main.loadFXML("Cart", "MainPage"));
            Main.setSceneToStage(cart, scene);
        } catch (IOException e) {
            show("Error", e.getMessage(), cart.getScene().getWindow(), true);
            e.printStackTrace();
        }
    }
}