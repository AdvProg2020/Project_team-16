package View.Controllers;

import ModelPackage.Off.OffStatus;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.System.exeption.off.NoSuchAOffException;
import ModelPackage.System.exeption.off.ThisOffDoesNotBelongssToYouException;
import View.CacheData;
import View.Main;
import View.PrintModels.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import controler.SellerController;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

import static ModelPackage.Off.OffStatus.*;

public class OffManager {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public JFXButton create;
    public JFXSlider createPercentage;
    public DatePicker crStartDt;
    public DatePicker crEndDate;

    public JFXSlider percent;
    public DatePicker startDate;
    public DatePicker endDate;
    public Label offStatus;

    public ListView<MiniProductPM> productsList;
    public ComboBox<MiniProductPM> availableProducts;
    public Button deleteProduct;
    public Button addProduct;

    public JFXButton confirm;
    public JFXButton reset;
    public JFXButton removeOff;

    public ListView<OffPM> offsList;

    private SellerController sellerController = SellerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();
    private final String username = cacheData.getUsername();

    @FXML
    public void initialize(){
        initButtons();
        initOffList();
    }

    private void initButtons() {
        minimize.setOnAction(e -> {
            Stage stage = (Stage) minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        close.setOnAction(e -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
        back.setOnAction(event -> handleBack());

        removeOff.setOnAction(event -> handleRemoveOff());
        removeOff.disableProperty().bind(Bindings.isEmpty(offsList.getSelectionModel().getSelectedItems()));

        confirm.setOnAction(event -> handleConfirm());
        confirm.setDisable(true);

        reset.setOnAction(event -> handleReset());
        reset.setDisable(true);

        deleteProduct.setOnAction(event -> handleDeleteProduct());
        deleteProduct.disableProperty().bind(Bindings.isEmpty(productsList.getSelectionModel().getSelectedItems()));

        addProduct.setOnAction(event -> handleAddProduct());
        addProduct.disableProperty().bind(availableProducts.valueProperty().isNull());

        create.setOnAction(event -> handleCreate());
    }

    private void handleCreate() {
        if (crStartDt.getValue().isAfter(crEndDate.getValue()) || crStartDt.getValue().isEqual(crEndDate.getValue())){
            //TODO: Show Error!!!
        } else {
            try {
                sellerController.addOff(
                        Date.from(Instant.from(crStartDt.getValue())),
                        Date.from(Instant.from(crEndDate.getValue())),
                        (int) createPercentage.getValue(),
                        username
                );
            } catch (ParseException | InvalidTimes | UserNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    private void initListeners() {
        percent.valueProperty().addListener(e -> {
            confirm.setDisable(false);
            reset.setDisable(false);
        });
        startDate.valueProperty().addListener(e -> {
            confirm.setDisable(false);
            reset.setDisable(false);
        });
        endDate.valueProperty().addListener(e -> {
            confirm.setDisable(false);
            reset.setDisable(false);
        });
    }

    private void handleBack() {
        try {
            Main.setRoot("SellerAccount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initOffList() {
        offsList.getItems().addAll(getOffs());

        //offsList.getItems().addAll(getTestOffs());

        offsList.getSelectionModel().selectedItemProperty().addListener( (v, oldOff, newOff) -> changeData(newOff));
    }

    private ObservableList<OffPM> getOffs(){
        ObservableList<OffPM> offs = FXCollections.observableArrayList();

        try {
            offs.addAll(sellerController.viewAllOffs(username, cacheData.getSorts()));
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }

        return offs;
    }

    private ObservableList<OffPM> getTestOffs() {
        ObservableList<OffPM> offs = FXCollections.observableArrayList();
        offs.add(new OffPM(
                12456,
                getTestProducts(),
                "marmof",
                new Date(2001, 12, 22),
                new Date(2002, 11, 30),
                50,
                EDIT.toString()
        ));
        offs.add(new OffPM(
                48662,
                getTestProducts(),
                "sapa",
                new Date(2012, 3, 13),
                new Date(2022, 6, 22),
                0,
                CREATION.toString()
        ));
        offs.add(new OffPM(
                21556,
                getTestProducts(),
                "memo",
                new Date(2021, 12, 22),
                new Date(2044, 6, 4),
                2,
                ACCEPTED.toString()
        ));

        return offs;
    }

    private ArrayList<MiniProductPM> getTestProducts() {
        ArrayList<MiniProductPM> products = new ArrayList<>();
        products.add(new MiniProductPM("asus rog g512", 112, "Laptop", "Asus", 5.42, null, null));
        products.add(new MiniProductPM("skirt for kimmi", 245, "Clothes", "Adidas", 5.42, null, null));
        products.add(new MiniProductPM("asus zenbook e333", 230, "Laptop", "Asus", 5.42, null, null));
        products.add(new MiniProductPM("asus vivobook d551", 7885, "Laptop", "Asus", 5.42, null, null));

        return products;
    }

    private void changeData(OffPM off) {
        offStatus.setText(off.getStatus());
        percent.setValue(off.getOffPercentage());
        startDate.setValue(LocalDate.parse(off.getStartTime().toString()));
        endDate.setValue(LocalDate.parse(off.getEndTime().toString()));

        productsList.getItems().clear();
        productsList.getItems().addAll(off.getProducts());

        availableProducts.getItems().clear();
        try {
            availableProducts.getItems().addAll(sellerController.manageProducts(username, cacheData.getSorts()));
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }

        initListeners();
        confirm.setDisable(true);
        reset.setDisable(true);
    }

    private void handleRemoveOff() {
        ObservableList<OffPM> offs = offsList.getItems();

        OffPM selected = offsList.getSelectionModel().getSelectedItem();
        offs.remove(selected);

        try {
            sellerController.deleteOff(selected.getOffId(), username);
        } catch (NoSuchAOffException | ThisOffDoesNotBelongssToYouException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteProduct() {
        ObservableList<MiniProductPM> products = productsList.getItems();

        MiniProductPM selected = productsList.getSelectionModel().getSelectedItem();
        products.remove(selected);

        OffChangeAttributes attributes = new OffChangeAttributes();
        attributes.setProductIdToRemove(selected.getId());
        try {
            sellerController.editOff(username, attributes);
        } catch (ThisOffDoesNotBelongssToYouException | NoSuchAOffException e) {
            e.printStackTrace();
        }
    }

    private void handleAddProduct() {
        MiniProductPM selected = availableProducts.getSelectionModel().getSelectedItem();
        OffChangeAttributes attributes = new OffChangeAttributes();
        attributes.setProductIdToAdd(selected.getId());
        try {
            sellerController.editOff(username, attributes);
        } catch (ThisOffDoesNotBelongssToYouException | NoSuchAOffException e) {
            e.printStackTrace();
        }

        productsList.getItems().add(selected);
        availableProducts.getItems().remove(selected);
    }

    private void handleConfirm() {
        OffChangeAttributes attributes = new OffChangeAttributes();
        addAttributes(attributes);

        try {
            sellerController.editOff(username, attributes);
        } catch (ThisOffDoesNotBelongssToYouException | NoSuchAOffException e) {
            e.printStackTrace();
        }

        confirm.setDisable(true);
        reset.setDisable(true);
    }

    private void addAttributes(OffChangeAttributes attributes) {
        OffPM off = offsList.getSelectionModel().getSelectedItem();

        if ((int)percent.getValue() != off.getOffPercentage()){
            attributes.setPercentage((int) percent.getValue());
        } else if (Date.from(Instant.from(startDate.getValue())).compareTo(off.getStartTime()) != 0){
            attributes.setStart(Date.from(Instant.from(startDate.getValue())));
        } else if (Date.from(Instant.from(endDate.getValue())).compareTo(off.getEndTime()) != 0){
            attributes.setEnd(Date.from(Instant.from(endDate.getValue())));
        }
    }

    private void handleReset() {
        OffPM off = offsList.getSelectionModel().getSelectedItem();

        percent.setValue(off.getOffPercentage());
        startDate.setValue(LocalDate.parse(off.getStartTime().toString()));
        endDate.setValue(LocalDate.parse(off.getEndTime().toString()));

        confirm.setDisable(true);
        reset.setDisable(true);
    }
}
