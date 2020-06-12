package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import View.CacheData;
import View.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controler.ProductController;
import controler.SellerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.ArrayList;

public class NewProduct {
    public ComboBox<String> category;
    public JFXTextField productName;
    public JFXTextField price;
    public JFXTextField stock;
    public JFXTextField company;
    public JFXTextArea description;
    public TableView table;
    public TableColumn Features;
    public TableColumn TextFields;
    public JFXButton createProduct;
    public JFXButton back;
    public Button view;
    public Button sellThis;
    public VBox sellThisBox;
    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");

    private ProductController productController = ProductController.getInstance();
    private SellerController sellerController = SellerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    @FXML
    public void initialize(){
        initButtons();
        initFields();
        initCategoryBox();
        initFeatureTable();
    }

    private void initCategoryBox() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        ArrayList<String> allCats = null; //TODO: Get All Categories!!!
        categories.addAll(allCats);

        category.setItems(categories);
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

    private void initFeatureTable() {
        //TODO: Init Table
    }

    private void initButtons() {
        createProduct.setOnAction(event -> handleCreate());
        view.setOnAction(event -> handleView());
        sellThis.setOnAction(event -> handleSellThis());
        back.setOnAction(event -> handleBack());
    }

    private void handleBack() {
        try {
            Main.setRoot("SellerAccount");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleSellThis() {
        //TODO: Add Seller To Product!!!
    }

    private void handleView() {
        try {
            Main.setRoot("ProductDigest");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Digest Menu!!!");
        }
    }

    private void handleCreate() {
        if (checkForEmptyValues()){
            String[] productInfo = new String[5];
            ArrayList<String> publicFeatures = new ArrayList<>();
            ArrayList<String> specialFeatures = new ArrayList<>();

            generateProductInfoPack(productInfo);
            generatePublicFeaturePack(publicFeatures);
            generateSpecialFeaturePack(specialFeatures);

            try {
                sellerController.addProduct(productInfo, publicFeatures.toArray(String[]::new), specialFeatures.toArray(String[]::new));
            } catch (NoSuchACategoryException e) {
                System.out.println("Category Not Found!!!");
            } catch (UserNotAvailableException e) {
                System.out.println("UserNotFound!!!");
            }
        }
    }

    private boolean checkForEmptyValues(){
        if (productName.getText().isEmpty()){
            errorField(productName,"Product Name Is Required");
            return false;
        } else if (price.getText().isEmpty()){
            errorField(price,"Password Is Required");
            return false;
        } else if (stock.getText().isEmpty()) {
            errorField(stock,"First Name Is Required");
            return false;
        } else if (company.getText().isEmpty()) {
            errorField(company,"Last Name Is Required");
            return false;
        } else if (!price.getText().matches("\\d+")){
            errorField(price,"Price Must Be Numerical");
            return false;
        } else if (!stock.getText().matches("\\d+")){
            errorField(price,"Stock Must Be Numerical");
            return false;
        }
        else return true;
    }

    private void errorField(JFXTextField field,String prompt){
        field.setPromptText(prompt);
        field.setFocusColor(redColor);
        field.requestFocus();
    }


    private void generateProductInfoPack(String[] infoPack) {
        infoPack[0] = cacheData.getUsername();
        infoPack[1] = productName.getText();
        infoPack[2] = company.getText();
        //infoPack[3] = null; // TODO : Get Category ID by Name!!!
        infoPack[4] = description.getText();
    }

    private void generatePublicFeaturePack(ArrayList<String> infoPack){
        //TODO: get public features from table
    }

    private void generateSpecialFeaturePack(ArrayList<String> infoPack){
        //TODO: get special features from table
    }

}
