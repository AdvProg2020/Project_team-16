package View.Controllers;

import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.PrintModels.FullProductPM;
import View.PrintModels.MiniProductPM;
import View.PrintModels.SellPackagePM;
import com.jfoenix.controls.JFXButton;
import controler.ProductController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EditProduct {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;

    public Rectangle imageViewer;
    public ListView<Image> imageList;
    public JFXButton addPicture;
    public JFXButton removePicture;
    public JFXButton submitPicChange;

    public TableView<FeatureRow> featureTable;
    public TableColumn<FeatureRow, String> featureCol;
    public TableColumn<FeatureRow, String> currentValCol;
    public TableColumn<FeatureRow, String> EdValCol;
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
        listeners();
        binds();
        /*
        loadComboBox();
        buttons();
        pictureSection();
        mainSection();
        featureSection();
        */
    }

    private void binds() {
        pictureSectionBinds();
        mainSectionBinds();
        featureSectionBinds();
    }

    private void featureSectionBinds() {
        editFeatureBtn.disableProperty().bind(Bindings.isEmpty(editVal.textProperty()));
        editVal.disableProperty().bind(Bindings.isEmpty(
                featureTable.getSelectionModel().getSelectedItems()
        ));
    }

    private void mainSectionBinds() {
        submitMain.disableProperty().bind(
                Bindings.isEmpty(name.textProperty())
                        .and(colorBox.showingProperty().not())
                        .and(Bindings.isEmpty(weigh.textProperty()))
                        .and(Bindings.isEmpty(dimension.textProperty()))
                        .and(Bindings.isEmpty(price.textProperty()))
                        .and(Bindings.isEmpty(stock.textProperty()))
        );
    }

    private void pictureSectionBinds() {
        removePicture.disableProperty().bind(Bindings.isEmpty(imageList.getSelectionModel().getSelectedItems()));
        submitPicChange.disableProperty().bind(Bindings.isEmpty(imageList.getItems()));
    }

    private void listeners() {
        pictureListener();
    }

    private void pictureListener() {
        imageList.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> {
            if (n != null) {
                imageViewer.setFill(new ImagePattern(n));
            }
        });
    }

    private void loadProduct() {
        try {
            this.pm = productController.viewAttributes(id);
            loadMainSection(pm.getProduct(), pm.getFeatures());
            loadPictureSection();
            loadFeatureSection(pm.getFeatures());
        } catch (NoSuchAProductException e) {
            new OopsAlert().show(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPictureSection() {
        ArrayList<Image> images = productController.loadImage(id);
        ObservableList<Image> data = FXCollections.observableArrayList(images);
        imageList.setItems(data);
    }

    private void loadFeatureSection(Map<String, String> features) {
        List<FeatureRow> list = generateRowsFromMap(features);
        ObservableList<FeatureRow> data = FXCollections.observableArrayList(list);
        featureCol.setCellValueFactory(new PropertyValueFactory<>("feature"));
        currentValCol.setCellValueFactory(new PropertyValueFactory<>("oldValue"));
        EdValCol.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        featureTable.setItems(data);
    }

    private List<FeatureRow> generateRowsFromMap(Map<String, String> features) {
        List<FeatureRow> list = new ArrayList<>();
        features.forEach((key, value) -> list.add(new FeatureRow(key, value)));
        return list;
    }

    private void loadMainSection(MiniProductPM pm, Map<String, String> features) {
        SellPackagePM sellPackagePM = pm.findPackageForSeller(CacheData.getInstance().getUsername());
        name.setPromptText(pm.getName());
        String weight = features.getOrDefault("Weigh", "Not Specified");
        String dim = features.getOrDefault("Dimension", "Not Specified");
        features.remove("Weigh");
        features.remove("Color");
        features.remove("Dimension");
        weigh.setPromptText(weight);
        dimension.setPromptText(dim);
        price.setPromptText("" + sellPackagePM.getPrice());
        stock.setPromptText("" + sellPackagePM.getStock());
    }

    public class FeatureRow {
        private String feature;
        private String oldValue;
        private String newValue;

        public FeatureRow(String feature, String oldValue) {
            this.feature = feature;
            this.oldValue = oldValue;
            this.newValue = "";
        }

        boolean is(String feature) {
            return this.feature.equals(feature);
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }
    }
}
