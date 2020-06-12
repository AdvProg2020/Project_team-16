package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import View.PrintModels.CategoryPM;
import View.PrintModels.MicroProduct;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controler.ManagerController;
import controler.ProductController;
import controler.SellerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewProduct {
    public ComboBox<CategoryPM> category;
    public JFXTextField productName;
    public JFXTextField price;
    public JFXTextField stock;
    public JFXTextField company;
    public JFXTextArea description;
    public TableView<TableFeatureRow> table;
    public TableColumn<TableFeatureRow,String> Features;
    public TableColumn<TableFeatureRow,TextField> TextFields;
    public JFXButton createProduct;
    public JFXButton back;
    public Button view;
    public Button sellThis;
    public VBox sellThisBox;
    public ListView<MicroProduct> similarProduct;

    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");

    private SellerController sellerController = SellerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    @FXML
    public void initialize(){
        initButtons();
        initFields();
        initCategoryBox();
        initializeListeners();
    }

    private void initializeListeners() {
        category.getSelectionModel().selectedItemProperty().addListener((v, oldValue,newValue)->{
            changeFeaturesTable(newValue.getId());
        });
        productName.focusedProperty().addListener((v, oldValue,newValue)->{
            if (!newValue)fullSellThis(productName.getText());
        });
        sellThis.disableProperty().bind(Bindings.isEmpty(similarProduct.getSelectionModel().getSelectedItems()));
        view.disableProperty().bind(Bindings.isEmpty(similarProduct.getSelectionModel().getSelectedItems()));
    }

    private void fullSellThis(String entry){
        if (entry.isEmpty()){
            sellThisBox.setDisable(true);
            similarProduct.setItems(FXCollections.observableArrayList());
        }else {
            ArrayList<MicroProduct> microProducts = ProductController.getInstance().findSimilarProductsByName(entry);
            if (microProducts.isEmpty()){
                sellThisBox.setDisable(true);
            } else {
                sellThisBox.setDisable(false);
                ObservableList<MicroProduct> data = FXCollections.observableArrayList(microProducts);
                similarProduct.setItems(data);
            }
        }
    }

    private void initCategoryBox() {
        ObservableList<CategoryPM> categories = FXCollections.observableArrayList();
        ArrayList<CategoryPM> allCats = ManagerController.getInstance().getAllCategories();
        categories.addAll(allCats);
        category.setItems(categories);
    }

    private ArrayList<CategoryPM> load() {
        CategoryPM categoryPM1 = new CategoryPM("Human",1,0);
        CategoryPM categoryPM2 = new CategoryPM("Ass",2,1);
        CategoryPM categoryPM3 = new CategoryPM("Arm",3,1);
        CategoryPM categoryPM4 = new CategoryPM("Butt",4,1);
        CategoryPM categoryPM5 = new CategoryPM("Animal",5,0);
        ArrayList<CategoryPM> arrayList = new ArrayList<>();
        arrayList.add(categoryPM1);
        arrayList.add(categoryPM2);
        arrayList.add(categoryPM3);
        arrayList.add(categoryPM4);
        arrayList.add(categoryPM5);
        return arrayList;
    }

    private ArrayList<String> loadFeature(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("color");
        arrayList.add("cascade");
        arrayList.add("dimention");
        arrayList.add("parameter");
        return arrayList;
    }

    private ArrayList<MicroProduct> loadMicro() {
        ArrayList<MicroProduct> list = new ArrayList<>();
        list.add(new MicroProduct("Product A",3215));
        list.add(new MicroProduct("Product B",324));
        list.add(new MicroProduct("Product C",422));
        list.add(new MicroProduct("Product D",8868));
        return list;
    }

    private void initFields() {
        resetSettingForFields(productName,"Product Name");
        resetSettingForFields(price,"Price");
        resetSettingForFields(stock,"Stock");
        resetSettingForFields(company,"Company");
    }

    private void resetSettingForFields(JFXTextField field,String prompt){
        field.textProperty().addListener(e->{
            field.setFocusColor(blueColor);
            field.setPromptText(prompt);
        });
    }

    private void changeFeaturesTable(int categoryId) {
        if (table.isDisable())table.setDisable(false);
        try {
            ArrayList<String> features = SellerController.getInstance().getSpecialFeaturesOfCat(categoryId);
            List<TableFeatureRow> list = new ArrayList<>();
            features.forEach(f -> list.add(new TableFeatureRow(f)));
            ObservableList<TableFeatureRow> data = FXCollections.observableList(list);
            Features.setCellValueFactory(new PropertyValueFactory<>("feature"));
            TextFields.setCellValueFactory(new PropertyValueFactory<>("value"));
            table.setItems(data);
        } catch (NoSuchACategoryException ignore) {}
    }

    private void initButtons() {
        createProduct.setOnAction(event -> handleCreate());
        view.setOnAction(event -> handleView());
        sellThis.setOnAction(event -> handleSellThis());
        back.setOnAction(event -> handleBack());
    }

    private void handleBack() {
        try {
            // TODO: 6/12/2020 Changing current scene
            Main.setRoot("SellerAccount");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleSellThis() {
        if (checkPriceStock()){
            int[] info = new int[3];
            info[0] = similarProduct.getSelectionModel().getSelectedItem().getId();
            info[1] = Integer.parseInt(stock.getText());
            info[2] = Integer.parseInt(price.getText());
            String username = cacheData.getUsername();
            try {
                sellerController.becomeSellerOfExistingProgram(info,username);
            } catch (NoSuchAProductException | UserNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPriceStock() {
        if (price.getText().isEmpty()){
            errorField(price,"Price Is Required");
            return false;
        } else if (!price.getText().matches("\\d{0,9}")){
            errorField(price,"Price Must Be Numerical");
            return false;
        } else if (stock.getText().isEmpty()) {
            errorField(stock,"Stock Is Required");
            return false;
        } else if (!stock.getText().matches("\\d{0,9}")){
            errorField(price,"Stock Must Be Numerical");
            return false;
        } else return true;
    }

    private void handleView() {
        int productId = similarProduct.getSelectionModel().getSelectedItem().getId();
        cacheData.setProductId(productId);
        Stage window = new Stage();
        try {
            Scene scene = new Scene(Main.loadFXML("ProductDigest"));
            window.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            window.initStyle(StageStyle.TRANSPARENT);
            window.show();
        } catch (IOException ignore) {}
    }

    private void handleCreate() {
        if (checkForEmptyValues()){
            String[] productInfo = new String[5];
            generateProductInfoPack(productInfo);
            ArrayList<String> publicFeatures = new ArrayList<>();
            ArrayList<String> privateFeatures = new ArrayList<>();
            generateFeaturePacks(publicFeatures,privateFeatures);
            String[] puFeature = new String[publicFeatures.size()];
            puFeature = publicFeatures.toArray(puFeature);
            String[] prFeature = new String[privateFeatures.size()];
            prFeature = privateFeatures.toArray(prFeature);
            try {
                sellerController.addProduct(productInfo,puFeature,prFeature);
            } catch (NoSuchACategoryException | UserNotAvailableException ignore) {}
        }
    }

    private boolean checkForEmptyValues(){
        if (productName.getText().isEmpty()){
            errorField(productName,"Product Name Is Required");
            return false;
        } else if (category.getSelectionModel().getSelectedItem() == null){
            category.setPromptText("Select A Category!!");
            category.requestFocus();
            return false;
        } else if (price.getText().isEmpty()){
            errorField(price,"Price Is Required");
            return false;
        } else if (!price.getText().matches("\\d{0,9}")){
            errorField(price,"Price Must Be Numerical");
            return false;
        } else if (stock.getText().isEmpty()) {
            errorField(stock,"Stock Is Required");
            return false;
        } else if (!stock.getText().matches("\\d{0,9}")){
            errorField(price,"Stock Must Be Numerical");
            return false;
        } else if (company.getText().isEmpty()) {
            errorField(company,"Company Is Required");
            return false;
        } else if (description.getText().isEmpty()){
            errorField(description,"Description Is Required");
            return false;
        }
        else return true;
    }

    private void errorField(JFXTextField field,String prompt){
        field.setPromptText(prompt);
        field.setFocusColor(redColor);
        field.requestFocus();
    }

    private void errorField(JFXTextArea area,String prompt) {
        area.setFocusColor(redColor);
        area.setPromptText(prompt);
        area.requestFocus();
    }

    private void generateProductInfoPack(String[] infoPack) {
        infoPack[0] = cacheData.getUsername();
        infoPack[1] = productName.getText();
        infoPack[2] = company.getText();
        infoPack[3] = Integer.toString(category.getSelectionModel().getSelectedItem().getId());
        infoPack[4] = description.getText();
    }

    private void generateFeaturePacks(ArrayList<String> publicFeatures,ArrayList<String> privateFeatures){
        ArrayList<TableFeatureRow> list = new ArrayList<>(table.getItems());
        ArrayList<String> pubTitle = (ArrayList<String>) SellerController.getInstance().getPublicFeatures();
        for (TableFeatureRow row : list) {
            String title = row.feature;
            String feature = row.value.getText();
            if (pubTitle.contains(title)){
                publicFeatures.add(title);
                publicFeatures.add(feature);
                pubTitle.remove(title);
            }else {
                privateFeatures.add(title);
                privateFeatures.add(feature);
            }
        }
    }

    public class TableFeatureRow {
        private String feature;
        private TextField value;
        TableFeatureRow(String feature){
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
    }
}
