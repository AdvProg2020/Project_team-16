package View.Controllers;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.SellLogPM;
import com.jfoenix.controls.JFXButton;
import controler.SellerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleHistory extends BackAbleController {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public TableView<SellLogPM> saleTable;
    public TableColumn<SellLogPM, Integer> sellNoCol;
    public TableColumn<SellLogPM, Date> sellDateCol;
    public Label totalSale;
    public VBox infoBox;
    public Label saleId;
    public Label productId;
    public JFXButton viewProduct;
    public Label date;
    public Label moneyGotten;
    public Label off;
    public Label buyer;
    public Label deliveryStatus;

    private final CacheData cacheData = CacheData.getInstance();
    private final SellerController sellerController = SellerController.getInstance();

    @FXML
    public void initialize() {
        loadInformation();
        initButtons();
        listeners();
        binds();
    }

    private void binds() {
        infoBox.disableProperty().bind(Bindings.isEmpty(saleTable.getSelectionModel().getSelectedItems()));
        viewProduct.disableProperty().bind(Bindings.isEmpty(productId.textProperty()));
    }

    private void listeners() {
        saleTable.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                loadInfoBox(newValue);
            }
        });
    }

    private void loadInfoBox(SellLogPM pm) {
        saleId.setText("" + pm.getId());
        productId.setText("" + pm.getProductId());
        date.setText(pm.getDate().toString());
        moneyGotten.setText("" + pm.getMoneyGotten() + "$");
        off.setText("" + pm.getDiscount() + "%");
        buyer.setText(pm.getBuyer());
        deliveryStatus.setText(pm.getDeliveryStatus().name());
    }

    private void loadInformation() {
        try {
            List<SellLogPM> list = logTest(); //sellerController.viewSalesHistory(cacheData.getUsername());
            sellNoCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            sellDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
            ObservableList<SellLogPM> data = FXCollections.observableArrayList(list);
            saleTable.setItems(data);
            loadTotalPrice(list);
            if (false) throw new UserNotAvailableException();
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void loadTotalPrice(List<SellLogPM> list) {
        long total = list.stream().mapToLong(SellLogPM::getMoneyGotten).sum();
        totalSale.setText("" + total + "$");
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

    private void initButtons() {
        back.setOnAction(e -> handleBackButton());
        minimize.setOnAction(e -> minimize());
        close.setOnAction(e -> close());
        viewProduct.setOnAction(event -> loadProduct());
    }

    private void loadProduct() {
        int id = Integer.parseInt(productId.getText());
        cacheData.setProductId(id);
        try {
            Scene scene = new Scene(Main.loadFXML("ProductDigest"));
            Stage stage = new Stage();
            Main.moveSceneOnMouse(scene, stage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
