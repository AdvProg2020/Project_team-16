package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Manager;
import View.CacheData;
import View.Main;
import View.PrintModels.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import controler.SellerController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class OffManager {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public JFXSlider createPercentage;
    public DatePicker crStartDt;
    public DatePicker crEndDate;
    public JFXSlider percent;
    public DatePicker startDate;
    public DatePicker endDate;
    public Label offStatus;
    public ListView<MiniProductPM> products;
    public Button deleteProduct;
    public ChoiceBox<MiniProductPM> availableProducts;
    public Button addProduct;
    public JFXButton removeOff;
    public ListView<OffPM> offs;

    private SellerController sellerController = SellerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

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
        removeOff.disableProperty().bind(Bindings.isEmpty(offs.getSelectionModel().getSelectedItems()));

        deleteProduct.setOnAction(event -> handleDeleteProduct());
        deleteProduct.disableProperty().bind(Bindings.isEmpty(products.getSelectionModel().getSelectedItems()));

        addProduct.setOnAction(event -> handleAddProduct());
        addProduct.disableProperty().bind(Bindings.isEmpty(availableProducts.getItems()));
    }

    private void handleBack() {
        try {
            Main.setRoot("SellerAccount");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void initOffList() {
        try {
            offs.getItems().addAll(sellerController.viewAllOffs(cacheData.getUsername(), cacheData.getSorts()));
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }

        offs.getSelectionModel().selectedItemProperty().addListener( (v, oldOff, newOff) -> changeData(newOff));
    }

    private void changeData(OffPM off) {
        offStatus.setText(off.getStatus());
        percent.setValue(off.getOffPercentage());
        startDate.setValue(LocalDate.parse(off.getStartTime().toString()));
        endDate.setValue(LocalDate.parse(off.getEndTime().toString()));
        initProducts();
    }

    private void initProducts() {

    }

    private void handleRemoveOff() {

    }

    private void handleDeleteProduct() {

    }

    private void handleAddProduct() {

    }
}
