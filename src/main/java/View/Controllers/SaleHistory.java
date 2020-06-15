package View.Controllers;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.SellLogPM;
import com.jfoenix.controls.JFXButton;
import controler.SellerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleHistory {
    @FXML
    private JFXButton back;
    @FXML
    private JFXButton minimize;
    @FXML
    private JFXButton close;
    @FXML
    private TableView<SellLogPM> listTable;
    @FXML
    private TableColumn<SellLogPM, Integer> sellNo;
    @FXML
    private TableColumn<SellLogPM, Integer> product;
    @FXML
    private TableColumn<SellLogPM, Date> date;
    @FXML
    private TableColumn<SellLogPM, Long> price;
    @FXML
    private TableColumn<SellLogPM, Integer> discount;
    @FXML
    private TableColumn<SellLogPM, DeliveryStatus> delStatus;
    @FXML
    private Label totalSell;
    /*@FXML
    private LineChart sell;
    @FXML
    private BarChart barProductChar;*/

    private final CacheData cacheData = CacheData.getInstance();
    private final SellerController sellerController = SellerController.getInstance();
    private List<SellLogPM> saleHistories;

    @FXML
    public void initialize() {
        initButtons();
        loadTable();
        updateTotalSales();
    }

    private List<SellLogPM> logTest() {
        SellLogPM sellLogPM = new SellLogPM(0, 12,
                13, 5, new Date(), "Ali", DeliveryStatus.DELIVERED);
        SellLogPM sellLogPM1 = new SellLogPM(1, 13,
                20, 8, new Date(12345678), "SAPA", DeliveryStatus.DEPENDING);
        ArrayList<SellLogPM> sellLogPMS = new ArrayList<>();
        sellLogPMS.add(sellLogPM); sellLogPMS.add(sellLogPM1);
        return sellLogPMS;
    }

    private void loadTable() {
        try {
            saleHistories = logTest(); //sellerController.viewSalesHistory(cacheData.getUsername());
            ObservableList<SellLogPM> data = FXCollections.observableArrayList(saleHistories);
            sellNo.setCellValueFactory(new PropertyValueFactory<>("id"));
            product.setCellValueFactory(new PropertyValueFactory<>("productId"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            price.setCellValueFactory(new PropertyValueFactory<>("moneyGotten"));
            discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            delStatus.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));
            listTable.setItems(data);
            if (false) throw new UserNotAvailableException();
        } catch (UserNotAvailableException ignore) {}
    }

    private void updateTotalSales() {
        long totalSale = 0L;
        for (SellLogPM saleHistory : saleHistories) {
            totalSale += saleHistory.getMoneyGotten();
        }
        totalSell.setText(totalSale + "$");
    }

    private void initButtons() {
        back.setOnAction(e -> handleBackButton());
        minimize.setOnAction(e -> minimize());
        close.setOnAction(e -> close());
    }

    private void close() {
        Stage window = (Stage) back.getScene().getWindow();
        window.close();
    }

    private void minimize() {
        Stage window = (Stage) back.getScene().getWindow();
        window.setIconified(true);
    }

    private void handleBackButton() {
        try {
            Main.setRoot("SellerAccount");
        } catch (IOException ignore) {}
    }
}
