package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Data;
import View.Main;
import View.PrintModels.MiniProductPM;
import View.PrintModels.OrderLogPM;
import View.PrintModels.OrderMiniLogPM;
import com.jfoenix.controls.JFXButton;
import controler.CustomerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class OrderHistory {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public Button viewProduct;
    public TableView<OrderMiniLogPM> orderTable;
    public TableColumn<OrderMiniLogPM, Integer> orderNoColumn;
    public TableColumn<OrderMiniLogPM, Data> dateColumn;
    public Label no;
    public Label date;
    public Label price;
    public Label discount;
    public Label delStatus;
    public TableView productsTable;
    public TableColumn pNameCol;
    public TableColumn pSellerCol;
    public TableColumn pPriceCol;

    private CustomerController customerController = CustomerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();
    private final String username = cacheData.getUsername();

    @FXML
    public void initialize(){
        initButts();
        initOrdersTable();
    }

    private void initButts() {
        minimize.setOnAction(e -> {
            Stage stage = (Stage) minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        close.setOnAction(e -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
        back.setOnAction(event -> handleBack());
        cartButt.setOnAction(event -> handleCart());
        viewProduct.setOnAction(event -> handleViewProduct());
    }

    private void handleViewProduct() {
        try {
            Main.setRoot("ProductDigest");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleCart() {
        try {
            Main.setRoot("Cart");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleBack() {
        try {
            Main.setRoot("CustomerAccount");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

}
