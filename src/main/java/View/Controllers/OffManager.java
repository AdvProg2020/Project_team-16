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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static ModelPackage.Off.OffStatus.*;

public class OffManager extends BackAbleController {
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
            Notification.show("Error", "Please Check the Dates!!!", back.getScene().getWindow(), true);
        } else {
            try {
                sellerController.addOff(
                        convertToDateViaInstant(crStartDt.getValue()),
                        convertToDateViaInstant(crEndDate.getValue()),
                        (int) createPercentage.getValue(),
                        username
                );
            } catch (ParseException | InvalidTimes | UserNotAvailableException e) {
                e.printStackTrace();
            }

            resetFields();
            Notification.show("Successful", "Your Request was Sent to The Manager!!!", back.getScene().getWindow(), false);
        }
    }

    private void resetFields() {
        createPercentage.setValue(0);
        crStartDt.getEditor().clear();
        crEndDate.getEditor().clear();
    }

    private java.util.Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
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
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
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

    private void changeData(OffPM off) {
        if (off == null) {
            return;
        }
        offStatus.setText(off.getStatus());
        percent.setValue(off.getOffPercentage());
        startDate.setPromptText(off.getStartTime().toString());
        endDate.setPromptText(off.getEndTime().toString());

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
        OffPM off = offsList.getSelectionModel().getSelectedItem();
        attributes.setSourceId(off.getOffId());
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
        attributes.setSourceId(offsList.getSelectionModel().getSelectedItem().getOffId());
        try {
            sellerController.editOff(username, attributes);
            Notification.show("Successful", "Your Request was Sent to The Manager!!!", back.getScene().getWindow(), false);
        } catch (ThisOffDoesNotBelongssToYouException | NoSuchAOffException e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
            e.printStackTrace();
        }
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

        Notification.show("Successful", "Your Request was Sent to The Manager!!!", back.getScene().getWindow(), false);
    }

    private void addAttributes(OffChangeAttributes attributes) {
        OffPM off = offsList.getSelectionModel().getSelectedItem();
        attributes.setSourceId(off.getOffId());

        if ((int)percent.getValue() != off.getOffPercentage()){
            attributes.setPercentage((int) percent.getValue());
        } else if (startDate.getValue() != null) {
            attributes.setStart(convertToDateViaInstant(startDate.getValue()));
        } else if (endDate.getValue() != null) {
            attributes.setEnd(convertToDateViaInstant(endDate.getValue()));
        }
    }
    private void handleReset() {
        OffPM off = offsList.getSelectionModel().getSelectedItem();

        percent.setValue(off.getOffPercentage());
        startDate.setValue(null);
        startDate.getEditor().clear();
        endDate.setValue(null);
        endDate.getEditor().clear();

        confirm.setDisable(true);
        reset.setDisable(true);
    }
}
