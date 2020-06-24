package View.Controllers;

import ModelPackage.Off.Off;
import ModelPackage.Product.Category;
import ModelPackage.Product.Company;
import ModelPackage.Product.Product;
import ModelPackage.Product.SellPackage;
import ModelPackage.System.SortType;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.filters.InvalidFilterException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import View.CacheData;
import View.FilterPackage;
import View.Main;
import View.PrintModels.MiniProductPM;
import View.PrintModels.SellPackagePM;
import View.SortPackage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import controler.ManagerController;
import controler.ProductController;
import controler.SellerController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProductPage {
    public ToggleGroup ADOrder;
    public ToggleGroup type;
    public JFXButton back ;
    public JFXButton minimize;
    public JFXButton close;
    public VBox panelVbox;
    public JFXComboBox<String> color;
    public JFXSlider minPrice;
    public JFXSlider maxPrice;
    // TODO: 6/20/2020 Fucking Full Of Problems Should Be Diagnose

    @FXML
    public void initialize() {
        initButtons();
        loadProducts();
    }

    private void loadProducts() {
        loadInformation();
        ADOrder.selectedToggleProperty().addListener((v, oldValue, newValue) ->
                loadInformation());
        type.selectedToggleProperty().addListener((v, oldValue, newValue) ->
                loadInformation());
        minPrice.setOnMouseDragged(mouseEvent -> loadInformation());
        minPrice.setOnMousePressed(mouseEvent -> loadInformation());
        maxPrice.setOnMousePressed(mouseEvent -> loadInformation());
        maxPrice.setOnMouseDragged(mouseEvent -> loadInformation());
        color.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                loadInformation());
    }

    private void createListsOfProductsInVBox(List<MiniProductPM> products) {
        for (MiniProductPM product : products) {
            try {
                panelVbox.getChildren().add(ProductRowForSM.generate(product.getName(), product.getId()));
            } catch (IOException ignore) {}
        }
    }

    private void loadInformation() {
        panelVbox.getChildren().clear();
        SortPackage sortPackage = makeSortPackage();
        FilterPackage filterPackage = makeFilterPackage();
        CacheData cacheData = CacheData.getInstance();
        try {
            switch (cacheData.getRole()) {
                case "Seller":
                case "seller":
                    List<MiniProductPM> productPMS = SellerController.getInstance().manageProducts(cacheData.getUsername() /*"Ali"*/, sortPackage, filterPackage);
                    createListsOfProductsInVBox(productPMS);
                    break;
                case "manager":
                case "Manager":
                    List<MiniProductPM> productPMSManager = ManagerController.getInstance().manageProducts(sortPackage, filterPackage);
                    createListsOfProductsInVBox(productPMSManager);
                    break;
            }

        } catch (UserNotAvailableException e) {
            new OopsAlert().show(e.getMessage());
        }
    }

    private FilterPackage makeFilterPackage() {
        FilterPackage filterPackage = new FilterPackage();
        if (color.getValue() != null) {
            filterPackage.getActiveFilters().put("Color", color.getValue());
        }
        if (minPrice.getValue() >= maxPrice.getValue()) {
            maxPrice.setValue(minPrice.getValue());
        }
        filterPackage.setCategoryId(0);
        filterPackage.setUpPriceLimit((int)maxPrice.getValue());
        filterPackage.setDownPriceLimit((int)minPrice.getValue());
        return filterPackage;
    }

    private SortPackage makeSortPackage() {
        SortPackage sortPackage = new SortPackage();
        sortPackage.setAscending(ADOrder.selectedToggleProperty().toString().contains("Ascending"));
        if (type.selectedToggleProperty().toString().contains("Price")) {
            if (sortPackage.isAscending())
                sortPackage.setSortType(SortType.MORE_PRICE);
            else sortPackage.setSortType(SortType.LESS_PRICE);
        } else if (type.selectedToggleProperty().toString().contains("Date Added")) {
            sortPackage.setSortType(SortType.TIME);
        } else if (type.selectedToggleProperty().toString().contains("View")) {
            sortPackage.setSortType(SortType.VIEW);
        } else if (type.selectedToggleProperty().toString().contains("Bought")) {
            sortPackage.setSortType(SortType.BOUGHT_AMOUNT);
        } else if (type.selectedToggleProperty().toString().contains("Name")) {
            sortPackage.setSortType(SortType.NAME);
        } else sortPackage.setSortType(SortType.SCORE);
        return sortPackage;
    }

    private void initButtons() {
        back.setOnAction(e -> handleBack());
        minimize.setOnAction(e -> minimize());
        close.setOnAction(e -> close());
    }

    private void close() {
        Stage window = (Stage) back.getScene().getWindow();
        window.close();;
    }

    private void minimize() {
        Stage window = (Stage) back.getScene().getWindow();
        window.setIconified(true);
    }

    private void handleBack() {
        try {
            Main.setRoot("ManagerAccount");
        } catch (IOException ignore) {}
    }
}
