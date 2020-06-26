package View.Controllers;

import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import View.PrintModels.FullProductPM;
import View.PrintModels.MicroProduct;
import View.PrintModels.MiniProductPM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controler.ProductController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompareProduct extends BackAbleController {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;

    public JFXComboBox<MicroProduct> choose;
    public Rectangle secondProductImage;

    public TableView<CompareRowFactory> table;
    public TableColumn<CompareRowFactory, String> feature;
    public TableColumn<CompareRowFactory, String> product1Col;
    public TableColumn<CompareRowFactory, String> product2Col;
    public ImageView mainImage;

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
        initChooseProduct();
        initProductsTable();
        loadFirstImage(mainProductId);
        binds();
    }

    private void loadFirstImage(int mainProductId) {
        Image image = productController.loadMainImage(mainProductId);
        mainImage.setImage(image);
    }

    private void binds() {
        cartButt.disableProperty().bind(cacheData.roleProperty.isEqualTo("Customer").not());
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

    private FullProductPM getTestProduct(boolean main) {
        if (main){
            return new FullProductPM(
                    new MiniProductPM(
                            "Asus Rog GL502VS",
                            123,
                            "Laptop",
                            "Asus",
                            1.22,
                            null,
                            null),
                    getFeatures(true)
            );
        } else {
            return new FullProductPM(
                    new MiniProductPM(
                            "Lenovo Ashghal 320RD",
                            54,
                            "Laptop",
                            "Lenovo",
                            0.22,
                            null,
                            null),
                    getFeatures(false)
            );
        }
    }

    private Map<String, String> getFeatures(boolean main) {
        Map<String, String> map = new HashMap<>();
        if (main) {
            map.put("color", "black");
            map.put("memory", "10g");
            map.put("pass", "high");
            map.put("size", "big");
        } else {
            map.put("color", "white");
            map.put("memory", "11g");
            map.put("pass", "low");
            map.put("size", "small");
        }
        return map;
    }

    private void initProductsTable() {
        initColumns();
        feature.setCellValueFactory(new PropertyValueFactory<>("feature"));
        product1Col.setCellValueFactory(new PropertyValueFactory<>("firstProduct"));

        addColumns();
        table.setItems(getRows());
    }

    private void initColumns() {
        feature = new TableColumn<>("Feature");
        feature.setMinWidth(183);
        feature.setStyle("-fx-alignment: CENTER;");
        product1Col = new TableColumn<>(mainProductPM.getProduct().getName());
        product1Col.setMinWidth(366);
        product1Col.setStyle("-fx-alignment: CENTER;");
        product2Col = new TableColumn<>();
        product2Col.setMinWidth(366);
        product2Col.setStyle("-fx-alignment: CENTER;");
    }

    private ObservableList<CompareRowFactory> getRows() {
        ObservableList<CompareRowFactory> rows = FXCollections.observableArrayList();

        Map<String, String> features = mainProductPM.getFeatures();
        features.keySet().forEach(feature -> rows.add(new CompareRowFactory(feature, features.get(feature), null)));

        return rows;
    }

    private void initChooseProduct() {
        choose.setItems(getProductsInCategory());
        //choose.getItems().add(new MicroProduct("Lenovo Ashghal 320RD",54));

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
        product2Col = new TableColumn<>(secondProductPM.getProduct().getName());
        product2Col.setCellValueFactory(new PropertyValueFactory<>("secondProduct"));
        product2Col.setMinWidth(366);
        product2Col.setStyle("-fx-alignment: CENTER;");

        ArrayList<CompareRowFactory> rows = new ArrayList<>(table.getItems());
        rows.forEach(row ->{
            Map<String, String> features = secondProductPM.getFeatures();
            for (String feature : features.keySet()) {
                if (feature.equals(row.feature)){
                    row.secondProduct = features.get(feature);
                    break;
                }
            }
        });
        ObservableList<CompareRowFactory> observableRows = FXCollections.observableArrayList(rows);

        addColumns();
        table.setItems(observableRows);
    }

    private void addColumns() {
        table.getColumns().clear();
        table.getColumns().add(feature);
        table.getColumns().add(product1Col);
        table.getColumns().add(product2Col);
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
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
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
    public class CompareRowFactory{
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
