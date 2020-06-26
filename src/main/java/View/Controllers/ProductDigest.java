package View.Controllers;

import ModelPackage.System.exeption.account.NoSuchACustomerException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import View.PrintModels.CommentPM;
import View.PrintModels.FullProductPM;
import View.PrintModels.MiniProductPM;
import View.PrintModels.SellPackagePM;
import View.exceptions.CanceledException;
import com.jfoenix.controls.JFXButton;
import controler.CustomerController;
import controler.ProductController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDigest extends BackAbleController {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public ImageView mainImage;
    public JFXButton video;
    public JFXButton nextPhoto;
    public JFXButton prePhoto;
    public JFXButton lastPhoto;
    public JFXButton firstPhoto;
    public Label productName;
    public Label company;
    public FontIcon star1;
    public FontIcon star2;
    public FontIcon star3;
    public FontIcon star4;
    public FontIcon star5;
    public Text description;
    public ComboBox<SellPackagePM> sellerBox;
    public Button addToCart;
    public Label price;
    public Label offPrice;
    public Button compare;
    public TableView<Map.Entry<String, String>> features;
    public VBox commentVBox;
    public JFXButton addComment;
    public ImageView specialOffer;
    public ImageView notAvailableSign;

    private int id;
    private static final CacheData cacheData = CacheData.getInstance();
    private static final ProductController productController = ProductController.getInstance();
    private ArrayList<Image> images;
    private FullProductPM fullProductPM;

    @FXML
    public void initialize() {
        id = CacheData.getInstance().getProductId();
        listeners();
        loadProduct();
        buttonInit();
        commentSection();
        binds();
    }

    private void binds() {
        cartButt.disableProperty().bind(cacheData.roleProperty.isEqualTo("Customer").not());

    }

    private void buttonInit() {
        upBarButtons();
        photoButtons();
        productButtons();
        commentButton();
        /*
            videoSection()
        */
    }

    private void commentButton() {
        addComment.setOnAction(event -> handleCreateComment());
    }

    private void handleCreateComment() {
        try {
            String[] comment = new CommentGetter().returnAComment();
            String[] info = {cacheData.getUsername(), comment[0], comment[1], "" + id};
            productController.assignComment(info);
        } catch (UserNotAvailableException | NoSuchAProductException | NoSuchACustomerException e) {
            new OopsAlert().show(e.getMessage());
        } catch (CanceledException e) {
            System.out.println(e.getMessage());
        }
    }

    private void productButtons() {
        compare.setOnAction(event -> gotoCompare());
        addToCart.setOnAction(event -> handleAdd());
    }

    private void handleAdd() {
        String role = cacheData.getRole();
        String sellerId = sellerBox.getSelectionModel().getSelectedItem().getSellerUsername();
        if (role.equalsIgnoreCase("customer")) {
            String[] info = {cacheData.getUsername(), "" + id, sellerId, "1"};
            try {
                productController.addToCart(info);
            } catch (Exception e) {
                new OopsAlert().show(e.getMessage());
            }
        } else if (role.isEmpty()) {
            cacheData.getCart().addToCart(fullProductPM.getProduct(), sellerBox.getSelectionModel().getSelectedItem());
        } else {
            new OopsAlert().show("You must be A Customer To Buy");
        }
    }

    private void gotoCompare() {
        try {
            Scene scene = new Scene(Main.loadFXML("ComparePage", backForForward("ProductDigest")));
            Main.setSceneToStage(back,scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void photoButtons() {
        firstPhoto.setOnAction(event -> goToFirstPhoto());
        lastPhoto.setOnAction(event -> goToLastPhoto());
        nextPhoto.setOnAction(event -> gotoNextPhoto());
        prePhoto.setOnAction(event -> gotoPrePhoto());
    }

    private void gotoPrePhoto() {
        Image current = mainImage.getImage();
        int currentIndex = images.indexOf(current);
        if (currentIndex > 0) {
            mainImage.setImage(images.get(currentIndex - 1));
        }
    }

    private void gotoNextPhoto() {
        Image current = mainImage.getImage();
        int currentIndex = images.indexOf(current);
        int lastIndex = images.size() - 1;
        if (currentIndex < lastIndex) {
            mainImage.setImage(images.get(currentIndex + 1));
        }
    }

    private void goToLastPhoto() {
        mainImage.setImage(images.get(images.size() - 1));
    }

    private void goToFirstPhoto() {
        mainImage.setImage(images.get(0));
    }

    private void upBarButtons() {
        back.setOnAction(event -> backHandle());
        close.setOnAction(event -> handleClose());
        minimize.setOnAction(event -> handleMinimize());
        cartButt.setOnAction(event -> gotoCart());
    }

    private void handleClose() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    private void handleMinimize() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.setIconified(true);
    }

    private void gotoCart() {
        try {
            Scene scene = new Scene(Main.loadFXML("Cart", backForForward("ProductDigest")));
            Main.setSceneToStage(new Stage(StageStyle.UNDECORATED), scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void backHandle() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void commentSection() {
        try {
            List<CommentPM> comments = productController.viewProductComments(id);
            loadComments(comments);
        } catch (NoSuchAProductException e) {
            e.printStackTrace();
        }
    }

    private void loadComments(List<CommentPM> comments) {
        for (CommentPM comment : comments) {
            Parent parent = loadComment(comment);
            if (parent != null) {
                commentVBox.getChildren().add(parent);
            }
        }
    }

    private Parent loadComment(CommentPM comment) {
        try {
            return Comment.generateComment(comment);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void listeners() {
        sellerBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> loadPricing(newValue));
    }

    private void loadPricing(SellPackagePM pm) {
        if (pm.getOffPercent() != 0) {
            double price = pm.getPrice() * (100 - pm.getOffPercent()) / 100;
            this.price.setText("" + price + "$");
            offPrice.setText("RealPrice : " + pm.getPrice() + "$");
            offPrice.setVisible(true);
            specialOffer.setVisible(true);
        } else {
            price.setText("" + pm.getPrice());
            offPrice.setVisible(false);
            specialOffer.setVisible(false);
        }
        if (pm.isAvailable()) {
            notAvailableSign.setVisible(false);
        } else {
            notAvailableSign.setVisible(true);
        }
    }

    private void loadProduct() {
        try {
            FullProductPM pm = productController.viewAttributes(id);
            CustomerController.getInstance().addViewDigest(id);
            fullProductPM = pm;
            loadInformation(pm.getProduct());
            loadImage();
            initialFeatures(pm.getFeatures());
        } catch (NoSuchAProductException e) {
            e.printStackTrace();
        }
    }

    private void initialFeatures(Map<String, String> features) {
        TableColumn<Map.Entry<String, String>, String> featureCol = new TableColumn<>("Features");
        featureCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        TableColumn<Map.Entry<String, String>, String> valueCol = new TableColumn<>("Features");
        valueCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        featureCol.setMinWidth(288);
        valueCol.setMinWidth(288);
        ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList(features.entrySet());
        this.features.setItems(data);
        this.features.getColumns().setAll(featureCol, valueCol);
    }

    private void loadImage() {
        images = productController.loadImage(id);
        mainImage.setImage(images.get(0));
    }

    private void loadInformation(MiniProductPM product) {
        productName.setText(product.getName());
        company.setText(product.getBrand());
        initStars(product.getScore());
        description.setText(product.getDescription());
        loadSellers(product.getSellPackagePMs());
    }

    private void loadSellers(List<SellPackagePM> pms) {
        ObservableList<SellPackagePM> data = FXCollections.observableArrayList(pms);
        sellerBox.setItems(data);
        sellerBox.getSelectionModel().selectFirst();
    }

    private void initStars(double score) {
        if (score < 1) {
            star1.setIconLiteral("fa-star-o");
        }
        if (score < 2) {
            star2.setIconLiteral("fa-star-o");
        }
        if (score < 3) {
            star3.setIconLiteral("fa-star-o");
        }
        if (score < 4) {
            star4.setIconLiteral("fa-star-o");
        }
        if (score < 5) {
            star5.setIconLiteral("fa-star-o");
        }
    }

}
