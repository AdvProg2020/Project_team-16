package View.Controllers;

import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.PrintModels.FullProductPM;
import View.PrintModels.MiniProductPM;
import View.PrintModels.SellPackagePM;
import com.jfoenix.controls.JFXButton;
import controler.ProductController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.Map;

public class EditProduct {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public Rectangle imageViewer;
    public ListView imageList;
    public JFXButton addPicture;
    public JFXButton removePicture;
    public JFXButton submitPicChange;
    public TableView featureTable;
    public TableColumn featureCol;
    public TableColumn currentValCol;
    public TableColumn EdValCol;
    public TextField editVal;
    public JFXButton editFeatureBtn;
    public HBox submitFeature;
    public JFXButton resetTable;
    public TextField name;
    public ChoiceBox<String> colorBox;
    public TextField weigh;
    public TextField dimension;
    public TextField price;
    public TextField stock;
    public JFXButton submitMain;

    private int id;
    private FullProductPM pm;
    private static ProductController productController = ProductController.getInstance();

    @FXML
    public void initialize() {
        id = CacheData.getInstance().getProductId();
        loadProduct();
        /*
        loadComboBox();
        listeners();
        buttons();
        pictureSection();
        mainSection();
        featureSection();
        */
    }

    private void loadProduct() {
        try {
            this.pm = productController.viewAttributes(id);
            loadMainSection(pm.getProduct(), pm.getFeatures());
            //todo : loadPictureSection();
            loadFeatureSection(pm.getFeatures());
        } catch (NoSuchAProductException e) {
            new OopsAlert().show(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMainSection(MiniProductPM pm, Map<String, String> features) {
        SellPackagePM sellPackagePM = pm.findPackageForSeller(CacheData.getInstance().getUsername());
        name.setPromptText(pm.getName());
        String weight = features.getOrDefault("Weight", "Not Specified");
        String dim = features.getOrDefault("Dimension", "Not Specified");
        features.remove("Weigh");
        features.remove("Color");
        features.remove("Dimension");
        weigh.setPromptText(weight);
        dimension.setPromptText(dim);
        price.setPromptText("" + sellPackagePM.getPrice());
        stock.setPromptText("" + sellPackagePM.getStock());
    }
}
