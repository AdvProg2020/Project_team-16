package View.Controllers;

import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import View.PrintModels.FullProductPM;
import View.PrintModels.MicroProduct;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controler.ProductController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.util.Map;

public class CompareProduct {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;

    public Label mainProduct;
    public JFXComboBox<MicroProduct> choose;
    public Rectangle mainProductImage;
    public Rectangle secondProductImage;

    public TableView<CompareRowFactory> table;
    public TableColumn<CompareRowFactory, String> feature;
    public TableColumn<CompareRowFactory, String> product1Col;
    public TableColumn<CompareRowFactory, String> product2Col;

    private ProductController productController = ProductController.getInstance();
    private CacheData cacheData = CacheData.getInstance();
    private final int mainProductId = cacheData.getProductId();
    private FullProductPM mainProductPM;
    private FullProductPM secondProductPM;
    private static final String productTemplate = "/Images/user-png-icon-male-user-icon-512.png";

    @FXML
    public void initialize(){
        initProduct(mainProductId, true);
        initButtons();
        initLabel();
        loadImage(mainProductImage, mainProductId);
        initChooseProduct();
        initProductsTable();
    }

    private void initLabel() {
        mainProduct.setText(mainProductPM.getProduct().getName());
    }

    private void initProduct(int id, boolean main) {
        try {
            if (main) {
                mainProductPM = productController.viewAttributes(id);
            } else {
                secondProductPM = productController.viewAttributes(id);
            }
        } catch (NoSuchAProductException e) {
            e.printStackTrace();
        }
    }

    private void initProductsTable() {
        feature.setCellValueFactory(new PropertyValueFactory<>("feature"));
        product1Col.setCellValueFactory(new PropertyValueFactory<>("firstProduct"));
        product2Col.setCellValueFactory(new PropertyValueFactory<>("secondProduct"));

        table.setItems(getRows());
    }

    private ObservableList<CompareRowFactory> getRows() {
        ObservableList<CompareRowFactory> rows = FXCollections.observableArrayList();

        Map<String, String> features = mainProductPM.getFeatures();
        features.keySet().forEach(feature -> rows.add(new CompareRowFactory(feature, features.get(feature), null)));

        return rows;
    }

    private void initChooseProduct() {
        choose.getItems().addAll(getProductsInCategory());
        choose.getSelectionModel().selectedItemProperty().addListener( (v, oldProduct, newProduct) -> updateCompareProduct(newProduct));
    }

    private ObservableList<MicroProduct> getProductsInCategory(){
        ObservableList<MicroProduct> products = FXCollections.observableArrayList();
        try {
            products.addAll(productController.getAllProductInCategoryByProductId(mainProductId));
        } catch (NoSuchACategoryException | NoSuchAProductException e) {
            e.printStackTrace();
        }
        return products;
    }

    private void updateCompareProduct(MicroProduct newProduct) {
        initProduct(newProduct.getId(), false);
        loadImage(secondProductImage, newProduct.getId());
        updateTable();
    }

    private void updateTable() {
        table.getItems().forEach(row ->{
            Map<String, String> features = secondProductPM.getFeatures();
            for (String feature : features.keySet()) {
                if (feature.equals(row.feature)){
                    row.secondProduct = features.get(feature);
                    break;
                }
            }
        });
    }

    private void initButtons() {
        back.setOnAction(event -> handleBack());
        close.setOnAction(event -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
        minimize.setOnAction(event -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.setIconified(true);
        });
    }

    private void handleBack() {
        try {
            Main.setRoot("ProductDigest");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImage(Rectangle container, int id) {
        Image image = productController.loadMainImage(id);
        if (image == null) {
            image = new Image(productTemplate);
        }
        container.setFill(new ImagePattern(image));
    }

    @Data
    static class CompareRowFactory{
        private String feature;
        private String firstProduct;
        private String secondProduct;

        public CompareRowFactory(String feature, String firstProduct, String secondProduct) {
            this.feature = feature;
            this.firstProduct = firstProduct;
            this.secondProduct = secondProduct;
        }

    }
}
