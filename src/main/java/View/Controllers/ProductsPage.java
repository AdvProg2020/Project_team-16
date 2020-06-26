package View.Controllers;

import ModelPackage.System.SortType;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.filters.InvalidFilterException;
import View.FilterPackage;
import View.Main;
import View.PrintModels.CategoryPM;
import View.PrintModels.MiniProductPM;
import View.PrintModels.OffProductPM;
import View.SortPackage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import controler.ManagerController;
import controler.ProductController;
import controler.SellerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductsPage extends BackAbleController {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public VBox filterVBox;
    public JFXSlider minPrice;
    public JFXSlider maxPrice;
    public ToggleGroup ADOrder;
    public ToggleGroup type;
    public VBox productVBox;
    public Button searchBut;
    public Button ignoreSearch;
    public JFXToggleButton offOnly;
    public ListView<CategoryPM> categories;
    public TableView<TableFeatureRow> featureTable;
    public TableColumn<TableFeatureRow, String> filterCol;
    public TableColumn<TableFeatureRow, TextField> fieldCol;
    public ScrollPane filterPane;

    private ProductController productController = ProductController.getInstance();

    private EventHandler<MouseEvent> productGetter = event ->
            loadProductOfCategory(categories.getSelectionModel().getSelectedItem().getId());

    @FXML
    private void initialize() {
        initLoad();
        ubButtons();
        binds();
        listeners();
        buttons();
    }

    private void buttons() {
        searchBut.setOnMouseClicked(productGetter);
        ignoreSearch.setOnAction(event -> resetFields());
    }

    private void resetFields() {
        featureTable.getItems().forEach(TableFeatureRow::reset);
        featureTable.refresh();
    }

    private void listeners() {
        categories.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            loadFeatures(newValue.getId());
            loadProductOfCategory(newValue.getId());
        });
        maxPrice.setOnMouseReleased(productGetter);
        minPrice.setOnMouseReleased(productGetter);
        offOnly.setOnMouseClicked(productGetter);
        ADOrder.selectedToggleProperty().addListener(observable -> productGetter.handle(null));
        type.selectedToggleProperty().addListener(observable -> productGetter.handle(null));
    }

    private void loadFeatures(int id) {
        if (featureTable.isDisable()) featureTable.setDisable(false);
        try {
            ArrayList<String> features = SellerController.getInstance().getSpecialFeaturesOfCat(id);
            List<TableFeatureRow> list = new ArrayList<>();
            features.forEach(f -> list.add(new TableFeatureRow(f)));
            ObservableList<TableFeatureRow> data = FXCollections.observableList(list);
            filterCol.setCellValueFactory(new PropertyValueFactory<>("feature"));
            fieldCol.setCellValueFactory(new PropertyValueFactory<>("value"));
            featureTable.setItems(data);
        } catch (NoSuchACategoryException ignore) {
        }
    }

    private void loadProductOfCategory(int id) {
        if (offOnly.isSelected()) {
            loadOffProducts(id);
        } else {
            loadNormalProducts(id);
        }
    }

    private void loadOffProducts(int id) {
        FilterPackage filter = createFilterPackage(id);
        SortPackage sorts = createSortPackage();
        List<OffProductPM> offs = productController.showAllOnOffProducts(filter, sorts);
        createOffProduct(offs);
    }

    private void createOffProduct(List<OffProductPM> offs) {
        productVBox.getChildren().clear();
        List<List<OffProductPM>> chopped = chopped(offs, 4);
        chopped.forEach(sub -> productVBox.getChildren().add(row4ForBoxOff(sub)));
    }

    private Node row4ForBoxOff(List<OffProductPM> sub) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        ArrayList<Parent> products = new ArrayList<>();
        sub.forEach(pm -> {
            try {
                Parent parent = SingleProductOnOff.generate(pm);
                products.add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        hBox.getChildren().addAll(products);
        return hBox;
    }

    private void loadNormalProducts(int id) {
        FilterPackage filter = createFilterPackage(id);
        SortPackage sorts = createSortPackage();
        try {
            List<MiniProductPM> list = productController.showAllProducts(sorts, filter);
            creatingMiniProducts(list);
        } catch (NoSuchACategoryException | InvalidFilterException e) {
            new OopsAlert().show(e.getMessage());
            e.printStackTrace();
        }
    }

    private void creatingMiniProducts(List<MiniProductPM> list) {
        productVBox.getChildren().clear();
        List<List<MiniProductPM>> subLists = chopped(list, 4);
        subLists.forEach(sublist -> productVBox.getChildren().add(row4ForBox(sublist)));
    }

    private static <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    /**
     * @param list should include 4 MiniProduct DTOs
     * @return {@link HBox} include 4 (Or Less) {@link SingleMiniProduct}
     */
    private HBox row4ForBox(List<MiniProductPM> list) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        ArrayList<Parent> products = new ArrayList<>();
        list.forEach(pm -> {
            try {
                Parent parent = SingleMiniProduct.generate(pm.getId(), pm.getName(), pm.getPrice(), pm.getOffPrice());
                products.add(parent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        hBox.getChildren().addAll(products);
        return hBox;
    }

    private SortPackage createSortPackage() {
        SortPackage sortPackage = new SortPackage();
        sortPackage.setAscending(((RadioButton) ADOrder.getSelectedToggle()).getText().equals("Ascending"));
        sortPackage.setSortType(SortType.valueOF(((RadioButton) type.getSelectedToggle()).getText()));
        return sortPackage;
    }

    private FilterPackage createFilterPackage(int id) {
        FilterPackage filterPackage = new FilterPackage(id);
        int max = (int) maxPrice.getValue();
        int min = (int) minPrice.getValue();
        if (max == -1) {
            min = 0;
            max = 0;
        } else if (min == -1) {
            min = 0;
        }
        filterPackage.setUpPriceLimit(max);
        filterPackage.setDownPriceLimit(min);
        filterPackage.setActiveFilters(CreateActiveFilter());
        filterPackage.setOffMode(offOnly.isSelected());
        return filterPackage;
    }

    private HashMap<String, String> CreateActiveFilter() {
        HashMap<String, String> filters = new HashMap<>();
        featureTable.getItems().forEach(row -> {
            if (!row.value.getText().isBlank()) {
                filters.put(row.feature, row.value.getText());
            }
        });
        return filters;
    }

    private void binds() {
        cartButt.disableProperty().bind(cacheData.roleProperty.isEqualTo("Customer").not().and(
                Bindings.isEmpty(cacheData.roleProperty).not()
        ));
        minPrice.valueProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.doubleValue() > maxPrice.getValue()) {
                maxPrice.setValue(newValue.doubleValue());
            }
        });
        maxPrice.valueProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.doubleValue() < minPrice.getValue()) {
                minPrice.setValue(newValue.doubleValue());
            }
        });
        filterPane.disableProperty().bind(Bindings.isEmpty(categories.getSelectionModel().getSelectedItems()));
    }

    private void ubButtons() {
        close.setOnAction(event -> ((Stage) close.getScene().getWindow()).close());
        minimize.setOnAction(event -> ((Stage) close.getScene().getWindow()).setIconified(true));
        back.setOnAction(event -> handleBack());
        cartButt.setOnAction(event -> handleCartButt());
    }

    private void handleCartButt() {
        try {
            Scene scene = new Scene(Main.loadFXML("Cart", backForForward("ProductsPage")));
            Main.setSceneToStage(cartButt, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBack() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLoad() {
        loadCategories();
    }

    private void loadCategories() {
        List<CategoryPM> list = ManagerController.getInstance().getAllCategories();
        ObservableList<CategoryPM> data = FXCollections.observableArrayList(list);
        categories.setItems(data);
    }

    public class TableFeatureRow {
        private String feature;
        private TextField value;

        TableFeatureRow(String feature) {
            this.feature = feature;
            value = new TextField();
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public TextField getValue() {
            return value;
        }

        public void setValue(TextField value) {
            this.value = value;
        }

        public void reset() {
            value.clear();
        }
    }
}
